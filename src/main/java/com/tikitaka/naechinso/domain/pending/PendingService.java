package com.tikitaka.naechinso.domain.pending;

import com.tikitaka.naechinso.domain.member.MemberRepository;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.pending.dto.PendingFindResponseDTO;
import com.tikitaka.naechinso.domain.pending.dto.PendingUpdateCreditImageRequestDTO;
import com.tikitaka.naechinso.domain.pending.dto.PendingUpdateMemberImageRequestDTO;
import com.tikitaka.naechinso.domain.pending.entity.Pending;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.BadRequestException;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 직장이나 학력 인증 정보 승인을 요청한다
     * */
    public PendingFindResponseDTO createPendingByCreditImage(PendingUpdateCreditImageRequestDTO dto) {
        //영속성 컨텍스트 가져오기
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Pending pending = Pending.builder()
                .member(member)
                .type(dto.getType())
                .images(dto.getImages())
                .build();

        pendingRepository.save(pending);
        return PendingFindResponseDTO.of(pending);
    }

    /**
     * 유저의 프로필 사진 승인을 요청한다
     * */
    public PendingFindResponseDTO createPendingByMemberImage(PendingUpdateMemberImageRequestDTO dto) {
        //영속성 컨텍스트 가져오기
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Pending pending = Pending.builder()
                .member(member)
                .type(dto.getType())
                .build();
        pending.updateImage(dto.getImages());

        pendingRepository.save(pending);
        return PendingFindResponseDTO.of(pending);
    }

    /**
     * 유저의 요청을 승인한다
     * */
    public PendingFindResponseDTO acceptPending(Member adminMember, Long pendingId) {
        Pending pending = pendingRepository.findById(pendingId)
                .orElseThrow(() -> new NotFoundException(ErrorCode._BAD_REQUEST));

        //유저 정보가 없는 가입 승인 요청
        if (pending.getMember() == null) {
            throw new BadRequestException(ErrorCode.USER_NOT_FOUND);
        }

        pending.accept(adminMember);
        pendingRepository.save(pending);

        return PendingFindResponseDTO.of(pending);
    }



    //대기 승인 중인 정보를 모두 가져온다
    public List<PendingFindResponseDTO> findAllByMemberIdAndIsAcceptedIsTrue(Long memberId) {
        return pendingRepository.findAllByMemberIdAndIsAcceptedIsTrue(memberId)
                .stream().map(PendingFindResponseDTO::of).collect(Collectors.toList());
    }

    public List<PendingFindResponseDTO> findAllByMemberId(Long memberId) {
        return pendingRepository.findAllByMemberId(memberId)
                .stream().map(PendingFindResponseDTO::of).collect(Collectors.toList());
    }
    public List<PendingFindResponseDTO> findAll() {
        return pendingRepository.findAll()
                .stream().map(PendingFindResponseDTO::of).collect(Collectors.toList());
    }
}
