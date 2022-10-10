package com.tikitaka.naechinso.domain.pending;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikitaka.naechinso.domain.member.MemberRepository;
import com.tikitaka.naechinso.domain.member.dto.*;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.pending.constant.PendingType;
import com.tikitaka.naechinso.domain.pending.dto.PendingFindResponseDTO;
import com.tikitaka.naechinso.domain.pending.dto.PendingRejectRequestDTO;
import com.tikitaka.naechinso.domain.pending.dto.PendingResponseDTO;
import com.tikitaka.naechinso.domain.pending.dto.PendingUpdateMemberImageRequestDTO;
import com.tikitaka.naechinso.domain.pending.entity.Pending;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.BadRequestException;
import com.tikitaka.naechinso.global.error.exception.ForbiddenException;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PendingService {

    private final MemberRepository memberRepository;
    private final PendingRepository pendingRepository;



//    /**
//     * 학력 인증 정보 검토를 요청한다
//     * */
//    public PendingFindResponseDTO readPendingByMember(Member authMember) {
//        //영속성 컨텍스트 가져오기
//        Member member = memberRepository.findById(authMember.getId())
//                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
//
//        return findAllByMemberId(member.getId());
//    }


    /**
     * 학력 인증 정보 검토를 요청한다
     * */
    public MemberCommonResponseDTO createPendingByEdu(Member authMember, MemberUpdateEduRequestDTO dto) {
        //영속성 컨텍스트 가져오기
        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        //이미 심사중인 요청이 있음
        if (member.getPending() != null) {
            member.getPending().forEach(pending -> {
                if (pending.getType() == PendingType.EDU && pending.getAdminId() == null)
                    throw new BadRequestException(ErrorCode.DUPLICATE_PENDING_REQUEST);
            });
        }

        Pending pending = Pending.builder()
                .member(member)
                .type(PendingType.EDU)
                .content(new JSONObject(dto).toString())
                .images(dto.getEduImage())
                .build();

        pendingRepository.save(pending);
        return MemberCommonResponseDTO.of(member);
    }


    /**
     * 직장 인증 정보 검토를 요청한다
     * */
    public MemberCommonResponseDTO createPendingByJob(Member authMember, MemberUpdateJobRequestDTO dto) {
        //영속성 컨텍스트 가져오기
        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        //이미 심사중인 요청이 있음
        if (member.getPending() != null) {
            member.getPending().forEach(pending -> {
                if (pending.getType() == PendingType.JOB && pending.getAdminId() == null)
                    throw new BadRequestException(ErrorCode.DUPLICATE_PENDING_REQUEST);
            });
        }

        Pending pending = Pending.builder()
                .member(member)
                .type(PendingType.JOB)
                .content(new JSONObject(dto).toString())
                .images(dto.getJobImage())
                .build();

        pendingRepository.save(pending);
        return MemberCommonResponseDTO.of(member);
    }

    /**
     * 유저의 프로필 사진 검토를 요청한다
     * */
    public MemberDetailResponseDTO createPendingByMemberImage(Member authMember, MemberUpdateImageRequestDTO dto) {
        //영속성 컨텍스트 가져오기
        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        //MemberDetail 이 없음. 정회원이 아님
        if (member.getDetail() == null) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_USER);
        }

        //이미 심사중인 요청이 있음
        if (member.getPending() != null) {
            member.getPending().forEach(pending -> {
                if (pending.getType() == PendingType.MEMBER && pending.getAdminId() == null)
                    throw new BadRequestException(ErrorCode.DUPLICATE_PENDING_REQUEST);
            });
        }

        Pending pending = Pending.builder()
                .member(member)
                .type(PendingType.MEMBER)
                .images(StringUtils.join(dto.getImages(), ","))
                .build();

        pendingRepository.save(pending);
        return MemberDetailResponseDTO.of(member);
    }

    /**
     * 유저의 요청을 승인하고 정보를 업데이트 한다
     * */
    public PendingFindResponseDTO acceptPending(Member adminMember, Long pendingId) {
        Pending pending = pendingRepository.findById(pendingId)
                .orElseThrow(() -> new NotFoundException(ErrorCode._BAD_REQUEST));

        Member pendingMember = pending.getMember();
        //유저 정보가 없는 가입 승인 요청
        if (pendingMember == null) {
            throw new BadRequestException(ErrorCode.USER_NOT_FOUND);
        }

        //이미 처리 완료된 요청
        if (pending.getAdminId() != null) {
            throw new BadRequestException(ErrorCode.PENDING_ALREADY_PROCESSED);
        }

        //어드민 유저가 아니면 접근 거부
//        if (adminMember.getRole().getDetail() != "ROLE_ADMIN") {
//            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
//        }

        //승인 하면 멤버의 인증 정보 또는 사진이 변경됨
        if (pending.getType() == PendingType.JOB) {
            MemberUpdateJobRequestDTO requestDTO = pending.getJobContent();
            pendingMember.updateJob(requestDTO);
        }
        else if (pending.getType() == PendingType.EDU) {
            MemberUpdateEduRequestDTO requestDTO = pending.getEduContent();
            pendingMember.updateEdu(requestDTO);
        }
        else if (pending.getType() == PendingType.MEMBER) {
            pendingMember.updateImage(pending.getImages());
        }

        pending.accept(adminMember);
        pendingRepository.save(pending);

        return PendingFindResponseDTO.of(pending);
    }

    /**
     * 유저의 요청을 거절하고 이유를 작성한다
     * */
    public PendingFindResponseDTO rejectPending(Member adminMember, Long pendingId, PendingRejectRequestDTO dto) {
        Pending pending = pendingRepository.findById(pendingId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PENDING_NOT_FOUND));

//        Member pendingMember = pending.getMember();
//        //유저 정보가 없는  요청
//        if (pendingMember == null) {
//            throw new BadRequestException(ErrorCode.USER_NOT_FOUND);
//        }

        //어드민 유저가 아니면 접근 거부
//        if (adminMember.getRole().getDetail() != "ROLE_ADMIN") {
//            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
//        }

        //이미 승인 되었거나 거부한 적 있는 요청
        if (pending.getAdminId() != null) {
            throw new BadRequestException(ErrorCode.PENDING_ALREADY_PROCESSED);
        }

        //요청을 거부함
        pending.reject(adminMember, dto.getReason(), dto.getRejectImages());
        pendingRepository.save(pending);

        return PendingFindResponseDTO.of(pending);
    }


    //대기 승인 중인 정보를 모두 가져온다
    public List<PendingFindResponseDTO> findAllByMemberIdAndIsAcceptedIsTrue(Long memberId) {
        return pendingRepository.findAllByMemberIdAndIsAcceptedIsTrue(memberId)
                .stream().map(PendingFindResponseDTO::of).collect(Collectors.toList());
    }

    public List<PendingResponseDTO> findAllByMemberId(Long memberId) {
        return pendingRepository.findAllByMemberId(memberId)
                .stream().map(PendingResponseDTO::of).collect(Collectors.toList());
    }

    public List<PendingFindResponseDTO> findAll() {
        return pendingRepository.findAll()
                .stream().map(PendingFindResponseDTO::of).collect(Collectors.toList());
    }
}
