package com.tikitaka.naechinso.domain.member;

import com.tikitaka.naechinso.domain.card.CardService;
import com.tikitaka.naechinso.domain.card.entity.Card;
import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.dto.*;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.member.entity.MemberDetail;
import com.tikitaka.naechinso.domain.pending.PendingService;
import com.tikitaka.naechinso.domain.pending.constant.PendingType;
import com.tikitaka.naechinso.domain.pending.dto.PendingFindResponseDTO;
import com.tikitaka.naechinso.domain.pending.dto.PendingUpdateMemberImageRequestDTO;
import com.tikitaka.naechinso.domain.recommend.entity.Recommend;
import com.tikitaka.naechinso.global.common.response.TokenResponseDTO;
import com.tikitaka.naechinso.global.config.security.dto.JwtDTO;
import com.tikitaka.naechinso.global.config.security.jwt.JwtTokenProvider;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.BadRequestException;
import com.tikitaka.naechinso.global.error.exception.ForbiddenException;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import com.tikitaka.naechinso.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PendingService pendingService;
    private final CardService cardService;
    private final MemberRepository memberRepository;
    private final MemberDetailRepository memberDetailRepository;

    public Member findByPhone(String phone) {
        return memberRepository.findByPhone(phone)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public Member findByMember(Member member) {
        return findByPhone(member.getPhone());
    }

    public List<MemberFindResponseDTO> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberFindResponseDTO::of).collect(Collectors.toList());
    }

    public MemberCommonResponseDTO readCommonMember(Member authMember) {
        Member member = findByMember(authMember);
        return MemberCommonResponseDTO.of(member);
    }

    public MemberCommonJoinResponseDTO joinCommonMember(String phone, MemberCommonJoinRequestDTO dto) {
        //이미 존재하는 유저일 경우 400
        Optional<Member> checkMember = memberRepository.findByPhone(phone);
        if(checkMember.isPresent()) {
            throw new BadRequestException(ErrorCode.USER_ALREADY_EXIST);
        }

        Member member = MemberCommonJoinRequestDTO.toCommonMember(phone, dto);
        memberRepository.save(member);

        TokenResponseDTO tokenResponseDTO
                = jwtTokenProvider.generateToken(new JwtDTO(phone, "ROLE_USER"));

        return MemberCommonJoinResponseDTO.of(member, tokenResponseDTO);
    }

    /**
     * @// TODO: 2022-10-07 이상한점 수정 필요 
     * */
    public MemberCommonJoinResponseDTO updateCommonMember(Member authMember, MemberUpdateCommonRequestDTO dto) {
        //없는 유저면 404
        Member member = findByMember(authMember);
        member.updateCommon(dto);
        memberRepository.save(member);

        TokenResponseDTO tokenResponseDTO
                = jwtTokenProvider.generateToken(new JwtDTO(member.getPhone(), "ROLE_USER"));

        return MemberCommonJoinResponseDTO.of(member);
    }

    public MemberDetailResponseDTO readDetail(Member member) {
        return MemberDetailResponseDTO.of(member);
    }


    /**
     * 랜덤 추천받은 상대의 프로필 카드를 가져오는 서비스 로직
     * ACTIVE 한 카드에만 접근 권한이 있음
     * */
    public MemberOppositeProfileResponseDTO readOppositeMemberDetailAndRecommendById(Member authMember, Long id) {
        //현재 ACTIVE 한 카드와 요청 id가 같지 않으면 에러
        Card activeCard = cardService.findByMemberAndIsActiveTrue(authMember);
        Long targetId = activeCard.getTargetId();
        if (!targetId.equals(id)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_PROFILE);
        }

        Member oppositeMember = findById(targetId);
        return MemberOppositeProfileResponseDTO.of(oppositeMember);
    }

    public MemberDetailResponseDTO createDetail(Member authMember, MemberDetailJoinRequestDTO dto) {
        //영속성 유지를 위한 fetch
        Member member = findByMember(authMember);

        //detail 정보가 있으면 이미 가입한 회원
        if (member.getDetail() != null) {
            throw new BadRequestException(ErrorCode.USER_ALREADY_EXIST);
        }

        //추천 받은 정보가 없는 회원
        if (member.getRecommendReceived() == null || member.getRecommendReceived().isEmpty()) {
            throw new ForbiddenException(ErrorCode.RECOMMEND_NOT_RECEIVED);
        }

        for (Recommend recommend : member.getRecommendReceived()) {
            //아직 추천인이 없는 상태면 무시
            if (recommend.getSender() == null) {
                continue;
            }

            //받은 추천사 중 한명이라도 인증이 완료된 상태면 정회원 가입 실행
            if (recommend.getSender().getJobAccepted() || recommend.getSender().getEduAccepted()) {
                MemberDetail detail = MemberDetail.of(member, dto);
                memberDetailRepository.save(detail);

                member.setDetail(detail);
                pendingService.createPendingByMemberImage(member, new MemberUpdateImageRequestDTO(dto.getImages()));
                return MemberDetailResponseDTO.of(detail);
            }
        }
        //추천서를 작성한 사람의 인증이 완료되지 않은 경우
        throw new UnauthorizedException(ErrorCode.RECOMMEND_SENDER_UNAUTHORIZED);
    }


    /**
     * 직업 정보 업데이트 요청 처리
     * 사진 필드는 Pending 에서 승인 후 처리한다
     * */
    public MemberCommonResponseDTO updateJobRequest(Member authMember, MemberUpdateJobRequestDTO dto){
//        member.updateJob(dto);
//        memberRepository.save(member);

        //직업 정보 승인 요청

        return pendingService.createPendingByJob(authMember, dto);
    }

    /**
     * 학력 정보 업데이트 요청 처리
     * 사진 필드는 Pending 에서 승인 후 처리한다
     * */
    public MemberCommonResponseDTO updateEduRequest(Member authMember, MemberUpdateEduRequestDTO dto){
        //학력 정보 승인 요청
        return pendingService.createPendingByEdu(authMember, dto);
    }

    /**
     * MemberDetail 의 프로필 이미지를 업로드 한다
     * */
    public MemberDetailResponseDTO updateImage(Member authMember, MemberUpdateImageRequestDTO dto){
        return pendingService.createPendingByMemberImage(authMember, dto);
    }


    public void validateToken(Member authMember) {
        if (authMember == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
    }

    public void validateFormalMember(Member authMember) {
        validateToken(authMember);
        if (authMember.getDetail() == null) {
            throw new UnauthorizedException(ErrorCode.FORBIDDEN_USER);
        }
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    /** 이미 추천받은 카드들에 있는 유저 ID 에 해당하지 않으며
     * 매개변수 성별과 값이 다른 유저 한명을 가져온다 */
    public Member findTopByIdNotInAndGenderNot(Collection<Long> ids, Gender gender) {
        return memberRepository.findTopByIdNotInAndGenderNot(ids, gender)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RANDOM_USER_NOT_FOUND));
    }

    public boolean existsById(Long memberId) {
        return memberRepository.existsById(memberId);
    }

}
