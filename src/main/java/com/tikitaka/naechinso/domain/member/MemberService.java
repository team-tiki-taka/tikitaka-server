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
import com.tikitaka.naechinso.domain.recommend.RecommendRepository;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendReceiverDTO;
import com.tikitaka.naechinso.domain.recommend.entity.Recommend;
import com.tikitaka.naechinso.global.common.request.TokenRequestDTO;
import com.tikitaka.naechinso.global.common.response.TokenResponseDTO;
import com.tikitaka.naechinso.global.config.security.dto.JwtDTO;
import com.tikitaka.naechinso.global.config.security.jwt.JwtTokenProvider;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.BadRequestException;
import com.tikitaka.naechinso.global.error.exception.ForbiddenException;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import com.tikitaka.naechinso.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final JwtTokenProvider jwtTokenProvider;
    private final PendingService pendingService;
    private final RecommendRepository recommendRepository;
    private final MemberRepository memberRepository;
    private final MemberDetailRepository memberDetailRepository;

    /**
     * 로그인 -> Fcm Token DB에 등록한다
     * */
    public MemberLoginResponseDTO login(Member authMember, MemberLoginRequestDTO requestDTO) {
        Member member = findByMember(authMember);

        //이미 로그인 된 상태
        if (!StringUtils.isBlank(member.getFcmToken())) {
            throw new BadRequestException(ErrorCode.USER_ALREADY_LOGGED_IN);
        }

        member.setFcmToken(requestDTO.getFcmToken());
        memberRepository.save(member);

        return new MemberLoginResponseDTO(requestDTO.getFcmToken());
    }

    /**
     * 강제 로그인 -> Fcm Token 을 교체한다
     * */
    public MemberLoginResponseDTO forceLogin(Member authMember, MemberLoginRequestDTO requestDTO) {
        Member member = findByMember(authMember);

        member.setFcmToken(requestDTO.getFcmToken());
        memberRepository.save(member);

        return new MemberLoginResponseDTO(requestDTO.getFcmToken());
    }

    /**
     * 로그아웃 -> Register Token 및 Fcm Token DB에서 삭제한다
     * */
    public MemberLoginResponseDTO logout(Member authMember) {
        Member member = findByMember(authMember);

        //이미 로그아웃 된 상태
        if (StringUtils.isBlank(member.getFcmToken())) {
            throw new BadRequestException(ErrorCode.USER_ALREADY_LOGGED_OUT);
        }

        //redis 에서 registerToken 삭제
        jwtTokenProvider.deleteRegisterToken(member.getPhone());

        //푸시 알림 등록 해제
        member.setFcmToken("");
        memberRepository.save(member);

        return new MemberLoginResponseDTO("");
    }

    /**
     * 로그인 -> Fcm Token DB에 등록한다
     * @// TODO: 2022/10/30 노션에 정리
     * */
    public MemberReissueResponseDTO reissue(String accessToken, String refreshToken) {
        String phone;

        if (!jwtTokenProvider.validateTokenExceptExpiration(accessToken)){
            throw new BadRequestException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        try {
            phone = jwtTokenProvider.parseClaims(accessToken).getSubject();
        } catch (Exception e) {
            throw new BadRequestException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Member authMember = findByPhone(phone);

        //가입 여부 (detail == null) 확인 후 받은 추천서 꺼내옴
        final Boolean hasDetail = authMember.getDetail() != null;

        //만약 정회원이 아니라면 받은 추천 목록을 가져옴
        List<RecommendReceiverDTO> recommendReceived;
        if (!hasDetail) {
            recommendReceived = recommendRepository.findAllByReceiverPhoneAndSenderNotNull(phone)
                    .stream().map(RecommendReceiverDTO::of).collect(Collectors.toList());
        } else {
            recommendReceived = null;
        }

        //유저 밴 여부 가져오기
        final Boolean isBanned = false;

        jwtTokenProvider.validateRefreshToken(phone, refreshToken);

        TokenResponseDTO tokenResponseDTO = jwtTokenProvider.generateToken(new JwtDTO(phone, authMember.getRole().toString()));

        return MemberReissueResponseDTO.builder()
                .accessToken(tokenResponseDTO.getAccessToken())
                .refreshToken(tokenResponseDTO.getRefreshToken())
                .recommendReceived(recommendReceived)
                .isActive(hasDetail)
                .isBanned(isBanned)
                .build();
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
     *  추천사를 받은 유저의 세부 회원 가입
     * @// TODO: 2022/11/05 기본 정보도 추가로 수정
     **/
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
                member.updateCommonInfo(dto);
                memberRepository.save(member);

                MemberDetail detail = MemberDetail.of(member, dto);
                memberDetailRepository.save(detail);

                member.setDetail(detail);

                /** 임시 정책 221105 */
//                pendingService.createPendingByMemberImage(member, new MemberUpdateImageRequestDTO(dto.getImages()));
                member.updateImage(dto.getImages());
                /** 임시 정책 221105 */

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
        //직업 정보 승인 요청
//        return pendingService.createPendingByJob(authMember, dto);
        /** 임시 정책 221105 */
        Member member = memberRepository.findByMember(authMember)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        member.updateJob(dto);
        return MemberCommonResponseDTO.of(member);
        /** 임시 정책 221105 */
    }

    /**
     * 학력 정보 업데이트 요청 처리
     * 사진 필드는 Pending 에서 승인 후 처리한다
     * */
    public MemberCommonResponseDTO updateEduRequest(Member authMember, MemberUpdateEduRequestDTO dto){
        //학력 정보 승인 요청
//        return pendingService.createPendingByEdu(authMember, dto);
        /** 임시 정책 221105 */
        Member member = memberRepository.findByMember(authMember)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        member.updateEdu(dto);
        return MemberCommonResponseDTO.of(member);
        /** 임시 정책 221105 */
    }

    /**
     * 동의 정보 여부를 수정한다
     * */
    public MemberAcceptsResponseDTO updateAccepts(Member authMember, MemberUpdateAcceptsRequestDTO dto){
        Member member = findByMember(authMember);
        member.updateAccepts(dto);
        memberRepository.save(member);
        return MemberAcceptsResponseDTO.of(member);
    }

    /**
     * MemberDetail 의 프로필 이미지를 업로드 한다
     * */
    public MemberDetailResponseDTO updateImage(Member authMember, MemberUpdateImageRequestDTO dto){
        /** 임시 정책 221105 */
//        return pendingService.createPendingByMemberImage(authMember, dto);
        Member member = memberRepository.findByMember(authMember)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        member.updateImage(dto.getImages());
        return MemberDetailResponseDTO.of(member);
        /** 임시 정책 221105 */
    }

    /**
     * soft delete (수정 필요)
     * */
    public Member delete(Member authMember){
        Member member = findByMember(authMember);

        member.setDeleted();
        member.getDetail().setDeleted();
        memberRepository.save(member);

        return member;
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
    public Member findTopByIdNotInAndGenderNotAndDetailNotNull(Collection<Long> ids, Gender gender) {
        List<Member> memberList = memberRepository.findByIdNotInAndGenderNotAndDetailNotNull(ids, gender);
        if (memberList.isEmpty()) {
            throw new NotFoundException(ErrorCode.RANDOM_USER_NOT_FOUND);
        }
        return memberList.get(new Random().nextInt(memberList.size()));
    }

    public boolean existsById(Long memberId) {
        return memberRepository.existsById(memberId);
    }


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
}
