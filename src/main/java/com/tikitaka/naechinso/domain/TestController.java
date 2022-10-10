package com.tikitaka.naechinso.domain;

import com.tikitaka.naechinso.domain.member.MemberRepository;
import com.tikitaka.naechinso.domain.member.MemberService;
import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.dto.MemberCommonJoinRequestDTO;
import com.tikitaka.naechinso.domain.member.dto.MemberCommonJoinResponseDTO;
import com.tikitaka.naechinso.domain.member.dto.MemberCommonResponseDTO;
import com.tikitaka.naechinso.domain.member.dto.MemberUpdateJobRequestDTO;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.pending.PendingService;
import com.tikitaka.naechinso.domain.recommend.RecommendService;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendBySenderRequestDTO;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendResponseDTO;
import com.tikitaka.naechinso.global.config.CommonApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 프론트에서 테스트를 원할하게 진행하기 위한 컨트롤러
 * QA 진행 후 삭제합니다
 * */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final MemberService memberService;

    private final MemberRepository memberRepository;
    private final RecommendService recommendService;
    private final PendingService pendingService;

    @GetMapping("/request-member")
    @ApiOperation(value = "[*TEST*] 추천사를 요청하는 유저를 생성하고 엑세스 토큰을 반환한다")
    public CommonApiResponse<Object> generateRecommendRequestUser(
    ) {
        MemberCommonJoinResponseDTO responseDTO = memberService.joinCommonMember(
                "01012345678",
                MemberCommonJoinRequestDTO.builder()
                        .age(25)
                        .gender(Gender.M)
                        .name("닉")
                        .acceptsInfo(true)
                        .acceptsLocation(true)
                        .acceptsMarketing(true)
                        .acceptsReligion(true)
                        .acceptsService(true)
                        .build());

        Member member = memberService.findByPhone(responseDTO.getPhone());
        recommendService.createRecommendRequest(member);

        return CommonApiResponse.of(responseDTO);
    }

    @GetMapping("/recommend-send")
    @ApiOperation(value = "[*TEST*] 추천사를 요청하는 유저를 생성하고 엑세스 토큰을 반환한다")
    public CommonApiResponse<Object> generateRecommendReceiver(
    ) {

        //sender
        MemberCommonJoinResponseDTO senderDTO = memberService.joinCommonMember(
                "01011112222",
                MemberCommonJoinRequestDTO.builder()
                        .age(25)
                        .gender(Gender.M)
                        .name("닉")
                        .acceptsInfo(true)
                        .acceptsLocation(true)
                        .acceptsMarketing(true)
                        .acceptsReligion(true)
                        .acceptsService(true)
                        .build());

        Member sender = memberService.findByPhone(senderDTO.getPhone());

        //sender
        MemberCommonJoinResponseDTO receiverDTO = memberService.joinCommonMember(
                "01012345678",
                MemberCommonJoinRequestDTO.builder()
                        .age(25)
                        .gender(Gender.M)
                        .name("닉")
                        .acceptsInfo(true)
                        .acceptsLocation(true)
                        .acceptsMarketing(true)
                        .acceptsReligion(true)
                        .acceptsService(true)
                        .build());

        RecommendResponseDTO recommend = recommendService.createRecommend(sender, RecommendBySenderRequestDTO
                .builder()
                .age(25)
                .period("1년")
                .appeal("짱")
                .phone("01012345678")
                .meet("CMC에서")
                .personality("최고")
                .gender(Gender.M)
                .name("닉")
                .build());


        return CommonApiResponse.of(receiverDTO);
    }


    @GetMapping("/create-job-pending")
    @ApiOperation(value = "[*TEST*] 직업 업데이트 요청하는 멤버를 생성하고 엑세스 토큰을 반환한다")
    public CommonApiResponse<Object> createJobPendingMember(
    ) {
        MemberCommonJoinResponseDTO responseDTO = memberService.joinCommonMember(
                "01012345678",
                MemberCommonJoinRequestDTO.builder()
                        .age(25)
                        .gender(Gender.M)
                        .name("닉")
                        .acceptsInfo(true)
                        .acceptsLocation(true)
                        .acceptsMarketing(true)
                        .acceptsReligion(true)
                        .acceptsService(true)
                        .build());
        Member member = memberService.findByPhone(responseDTO.getPhone());

        pendingService.createPendingByJob(member, MemberUpdateJobRequestDTO.builder()
                .jobName("직장명").jobPart("개발자").jobLocation("강남").jobImage("img1").build());

        return CommonApiResponse.of(responseDTO);

    }


    @GetMapping("/create-job-pending-and-recommend-other")
    @ApiOperation(value = "[*TEST*] 직업 업데이트 요청하는 멤버를 생성하고 유저 한명을 추천한 뒤 다른 유저의 엑세스 토큰을 반환한다")
    public CommonApiResponse<Object> createJobPendingMemberAndRecommendOther(
    ) {
        MemberCommonJoinResponseDTO senderDTO = memberService.joinCommonMember(
                "01012345678",
                MemberCommonJoinRequestDTO.builder()
                        .age(25)
                        .gender(Gender.M)
                        .name("닉")
                        .acceptsInfo(true)
                        .acceptsLocation(true)
                        .acceptsMarketing(true)
                        .acceptsReligion(true)
                        .acceptsService(true)
                        .build());
        Member member1 = memberService.findByPhone(senderDTO.getPhone());

        pendingService.createPendingByJob(member1, MemberUpdateJobRequestDTO.builder()
                .jobName("직장명").jobPart("개발자").jobLocation("강남").jobImage("img1").build());

        MemberCommonJoinResponseDTO receiverDTO = memberService.joinCommonMember(
                "01011111111",
                MemberCommonJoinRequestDTO.builder()
                        .age(25)
                        .gender(Gender.M)
                        .name("닉")
                        .acceptsInfo(true)
                        .acceptsLocation(true)
                        .acceptsMarketing(true)
                        .acceptsReligion(true)
                        .acceptsService(true)
                        .build());
        Member member2 = memberService.findByPhone(receiverDTO.getPhone());

        System.out.println("recommend = " + recommendService.createRecommend(member1, RecommendBySenderRequestDTO
                .builder()
                .age(25)
                .period("1년")
                .appeal("짱")
                .phone("01011111111")
                .meet("CMC에서")
                .personality("최고")
                .gender(Gender.M)
                .name("닉")
                .build()));

        return CommonApiResponse.of(receiverDTO);

    }
//
//    @GetMapping("/create-detail-member")
//    @ApiOperation(value = "[*TEST*] 추천사를 요청하는 유저를 생성하고 엑세스 토큰을 반환한다")
//    public CommonApiResponse<Object> createDetailMember(
//    ) {
//        MemberCommonJoinResponseDTO responseDTO = memberService.joinCommonMember(
//                "01012345678",
//                MemberCommonJoinRequestDTO.builder()
//                        .age(25)
//                        .gender(Gender.M)
//                        .name("닉")
//                        .acceptsInfo(true)
//                        .acceptsLocation(true)
//                        .acceptsMarketing(true)
//                        .acceptsReligion(true)
//                        .acceptsService(true)
//                        .build());
//        Member member = memberService.findByPhone(responseDTO.getPhone());
//
//        return CommonApiResponse.of(memberService.createDetail(member, MemberDetailJoinRequestDTO.builder()
//                .height(180)
//                .address("우리집")
//                .religion("무교")
//                .drink("자주마심")
//                .smoke("비흡연자")
//                .mbti("ESTJ")
//                .personality("짱좋음")
//                .introduce("방갑습니다")
//                .hobby("기타")
//                .style("짱좋음")
//                .images(List.of("img1, img2, img3"))
//                .build()));
//
//    }

    @GetMapping("/set-admin-member/{id}")
    @ApiOperation(value = "[*TEST*] 특정 고유 아이디의 유저를 어드민으로 만든다")
    public CommonApiResponse<Object> createJobPendingMemberAndRecommendOther(
            @PathVariable("id") Long id
    ) {
        Member member = memberService.findById(id);
        member.setAdmin();

        memberRepository.save(member);

        return CommonApiResponse.of(MemberCommonResponseDTO.of(member));
    }
}
