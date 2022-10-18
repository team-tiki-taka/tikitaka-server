package com.tikitaka.naechinso.domain;

import com.tikitaka.naechinso.domain.card.CardRepository;
import com.tikitaka.naechinso.domain.card.CardService;
import com.tikitaka.naechinso.domain.member.MemberDetailRepository;
import com.tikitaka.naechinso.domain.member.MemberRepository;
import com.tikitaka.naechinso.domain.member.MemberService;
import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.dto.*;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.member.entity.MemberDetail;
import com.tikitaka.naechinso.domain.pending.PendingService;
import com.tikitaka.naechinso.domain.recommend.RecommendRepository;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    private final MemberDetailRepository memberDetailRepository;
    private final RecommendService recommendService;
    private final RecommendRepository recommendRepository;
    private final PendingService pendingService;
    private final CardService cardService;
    private final CardRepository cardRepository;

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

    @GetMapping("/create-cards-each")
    @ApiOperation(value = "[*TEST*] 생성한 두 유저의 카드를 각각 생성한다")
    public CommonApiResponse<Object> createCardsEach(
    ) {
        Member member1 = memberService.findByPhone("01011111111");
        Member member2 = memberService.findByPhone("01012345678");

        cardService.createCard(member1);
        cardService.createCard(member1);
        cardService.createCard(member2);
        cardService.createCard(member2);

        return CommonApiResponse.of(true);

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

    @GetMapping("/create-two-detail-user")
    @ApiOperation(value = "[*TEST*] 멤버 DB를 초기화한 후, 정회원으로 가입한 유저를 둘 생성하고 그 중 첫 번째 유저의 엑세스 토큰을 반환한다")
    public CommonApiResponse<Object> createTwoRegularMember() {

        memberRepository.deleteAll();
        recommendRepository.deleteAll();

        MemberCommonJoinResponseDTO senderDTO = memberService.joinCommonMember(
                "01012345678",
                MemberCommonJoinRequestDTO.builder()
                        .age(25)
                        .gender(Gender.W)
                        .name("아이유")
                        .acceptsInfo(true)
                        .acceptsLocation(true)
                        .acceptsMarketing(true)
                        .acceptsReligion(true)
                        .acceptsService(true)
                        .build());
        Member member1 = memberService.findByPhone(senderDTO.getPhone());
        member1.updateEdu(MemberUpdateEduRequestDTO.builder()
                        .eduImage("eduimage.jpg")
                        .eduLevel("대학교")
                        .eduMajor("컴공")
                        .eduName("홍익")
                .build());
        memberRepository.save(member1);

        MemberCommonJoinResponseDTO receiverDTO = memberService.joinCommonMember(
                "01011111111",
                MemberCommonJoinRequestDTO.builder()
                        .age(28)
                        .gender(Gender.M)
                        .name("지드래곤")
                        .acceptsInfo(true)
                        .acceptsLocation(true)
                        .acceptsMarketing(true)
                        .acceptsReligion(true)
                        .acceptsService(true)
                        .build());
        Member member2 = memberService.findByPhone(receiverDTO.getPhone());
        member2.updateJob(MemberUpdateJobRequestDTO.builder()
                        .jobName("티키타카")
                        .jobPart("프론트")
                        .jobImage("jobImage.png")
                        .jobLocation("강남")
                .build());
        memberRepository.save(member2);

        recommendService.createRecommend(member2, RecommendBySenderRequestDTO
                .builder()
                .age(25)
                .period("3달")
                .appeal("멋짐")
                .appealDetail("짱짱짱 멋짐")
                .phone("01012345678")
                .meet("테스트중에")
                .personality("착함")
                .gender(Gender.M)
                .name("박스")
                .build());
        List<RecommendResponseDTO> recommend1 = recommendService.findAllBySenderPhone(member2.getPhone());

        recommendService.createRecommend(member1, RecommendBySenderRequestDTO
                .builder()
                .age(25)
                .period("1년")
                .appeal("짱")
                .appealDetail("짱짱짱 멋짐")
                .phone("01011111111")
                .meet("CMC에서")
                .personality("최고")
                .gender(Gender.M)
                .name("닉")
                .build());


        memberDetailRepository.save(
                MemberDetail.builder()
                        .member(member1)
                        .address("서울시 강남구")
                        .drink("1병")
                        .height(180)
                        .hobby("서버 코딩")
                        .images("img1.jpg, profile.png, ee.jpg")
                        .image_accepted(true)
                        .introduce("반갑습니다")
                        .mbti("ESTJ")
                        .personality("직관적")
                        .religion("무교")
                        .smoke("비흡연자")
                        .style("좋음")
                        .build()
        );

        memberDetailRepository.save(
                MemberDetail.builder()
                        .member(member2)
                        .address("경기도 일산시")
                        .drink("반잔")
                        .height(195)
                        .hobby("프론트엔드 코딩")
                        .images("imppp.jpg, ppap.png, good.jpg")
                        .introduce("안녕하세요")
                        .mbti("INTJ")
                        .personality("직관적")
                        .religion("무교")
                        .smoke("비흡연자")
                        .style("개굿")
                        .build()
        );


        return CommonApiResponse.of(senderDTO);
    }




    @GetMapping("/create-multiple-users")
    @ApiOperation(value = "[*TEST*] 멤버 DB를 초기화한 후, 정회원으로 가입한 유저를 남녀 10명씩 생성하고, 멤버 하나의 엑세스 토큰 반환")
    public CommonApiResponse<Object> createMultipleMembers() {

        memberRepository.deleteAll();
        recommendRepository.deleteAll();

        List<Object> adminMemberInfo = Arrays.asList("01012345678", 25, Gender.M, "노경닉", "edu-image-admin.jpg", "홍익", "대학교", "컴퓨터공학과"); //어드민 겸 추천인


        Member adminMember = memberService.findByPhone(memberService.joinCommonMember(
                (String) adminMemberInfo.get(0),
                MemberCommonJoinRequestDTO.builder()
                        .age((int) adminMemberInfo.get(1))
                        .gender((Gender) adminMemberInfo.get(2))
                        .name((String) adminMemberInfo.get(3))
                        .acceptsInfo(true)
                        .acceptsLocation(true)
                        .acceptsMarketing(true)
                        .acceptsReligion(true)
                        .acceptsService(true)
                        .build()).getPhone());
        adminMember.updateEdu(MemberUpdateEduRequestDTO.builder()
                .eduImage((String) adminMemberInfo.get(4))
                .eduName((String) adminMemberInfo.get(5))
                .eduLevel((String) adminMemberInfo.get(6))
                .eduMajor((String) adminMemberInfo.get(7))
                .build());
        adminMember.setAdmin();
        memberRepository.save(adminMember);

        final List<List<Object>> joinRequestList = Arrays.asList(
                Arrays.asList("01011111111", 25, Gender.M, "허시준", "edu-image001.jpg", "서강", "대학교", "컴퓨터공학과", "1~3년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 180, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인, 상냥한, 섬세한", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01012222222", 26, Gender.M, "민성진", "edu-image002.jpg", "한국", "고등학교", "자동차정비", "1~3년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 185, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "INFP", "열정적인, 상냥한, 섬세한", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01013333333", 27, Gender.M, "김상혁", "edu-image003.jpg", "서울", "대학원", "인공지능공학", "1~3년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 182, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인, 상냥한, 섬세한", "기독교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01014444444", 28, Gender.M, "권영성", "edu-image004.jpg", "홍익", "대학교", "시각디자인학과", "1~3년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 183, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "INFJ", "열정적인, 상냥한, 섬세한", "불교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01015555555", 29, Gender.M, "배규빈", "edu-image005.jpg", "한성", "고등학교", "상업", "3~5년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 178, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인, 상냥한, 섬세한", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01016666666", 30, Gender.M, "권민기", "edu-image006.jpg", "서울", "고등학교", "디자인", "3~5년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 172, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESFJ", "열정적인, 상냥한, 섬세한", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01017777777", 31, Gender.M, "김민성", "edu-image007.jpg", "연세", "대학원", "영어영문학과", "1~3년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 173, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ENFP", "열정적인, 상냥한, 섬세한", "기독교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01018888888", 32, Gender.M, "임정혁", "edu-image008.jpg", "연세", "대학교", "전기전자공학과", "1~3년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 175, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인, 상냥한, 섬세한", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01019999999", 33, Gender.M, "성재오", "edu-image009.jpg", "이대부속", "고등학교", "공학", "1~3년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 168, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인, 상냥한, 섬세한", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01010000000", 25, Gender.M, "차재훈", "edu-image010.jpg", "국민", "대학교", "미디어학과", "1~3년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 176, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTP", "열정적인, 상냥한, 섬세한", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),

                Arrays.asList("01022200000", 25, Gender.W, "민장효", "edu-image011.jpg", "서강", "대학원", "물류학과", "1~3년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 160, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "INFP", "열정적인, 상냥한, 섬세한", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01022211111", 25, Gender.W, "김민서", "edu-image012.jpg", "홍익", "대학교", "조소과", "1~3년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 162, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인, 상냥한, 섬세한", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01022222222", 25, Gender.W, "노혜지", "edu-image013.jpg", "한국", "고등학교", "디자인", "1~3년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 165, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ENFP", "열정적인, 상냥한, 섬세한", "기독교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01022233333", 25, Gender.W, "류라해", "edu-image014.jpg", "이화여자", "대학교", "사이버보안학과", "1~3년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 155, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인, 상냥한, 섬세한", "기독교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01022244444", 25, Gender.W, "민예지", "edu-image015.jpg", "이화여자", "대학교", "국어국문학과", "3~5년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 150, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "INFJ", "열정적인, 상냥한, 섬세한", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01022255555", 25, Gender.W, "류유주", "edu-image016.jpg", "한국", "고등학교", "공학", "1~3년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 174, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "INTP", "열정적인, 상냥한, 섬세한", "기독교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01022266666", 25, Gender.W, "임혜서", "edu-image017.jpg", "서울", "대학원", "법학과", "1~3년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 172, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인, 상냥한, 섬세한", "불교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01022277777", 25, Gender.W, "임한하", "edu-image018.jpg", "서울", "대학교", "의상학과", "3~5년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 168, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ENTP", "열정적인, 상냥한, 섬세한", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01022288888", 25, Gender.W, "권민영", "edu-image019.jpg", "고려", "대학교", "의학", "1~3년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 175, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESFJ", "열정적인, 상냥한, 섬세한", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01022299999", 25, Gender.W, "진하수", "edu-image020.jpg", "연세", "대학교", "기계공학과", "1~3년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 163, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인, 상냥한, 섬세한", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어")
        );

        joinRequestList.forEach(info -> {
            MemberCommonJoinResponseDTO senderDTO = memberService.joinCommonMember(
                    (String) info.get(0),
                    MemberCommonJoinRequestDTO.builder()
                            .age((int) info.get(1))
                            .gender((Gender) info.get(2))
                            .name((String) info.get(3))
                            .acceptsInfo(true)
                            .acceptsLocation(true)
                            .acceptsMarketing(true)
                            .acceptsReligion(true)
                            .acceptsService(true)
                            .build());

            Member member = memberService.findByPhone(senderDTO.getPhone());

            member.updateEdu(MemberUpdateEduRequestDTO.builder()
                    .eduImage((String) info.get(4))
                    .eduName((String) info.get(5))
                    .eduLevel((String) info.get(6))
                    .eduMajor((String) info.get(7))
                    .build());

            memberRepository.save(member);
            memberRepository.flush();

            recommendService.createRecommend(adminMember, RecommendBySenderRequestDTO
                    .builder()
                    .phone((String) info.get(0))
                    .age((int) info.get(1))
                    .gender((Gender) info.get(2))
                    .name((String) info.get(3))
                    .period((String) info.get(8))
                    .meet((String) info.get(9))
                    .personality((String) info.get(10))
                    .appeal((String) info.get(11))
                    .appealDetail((String) info.get(12))
                    .build());


            memberDetailRepository.save(
                    MemberDetail.builder()
                            .member(member)
                            .address((String) info.get(13))
                            .drink((String) info.get(14))
                            .height((int) info.get(15))
                            .hobby((String) info.get(16))
                            .images((String) info.get(17))
                            .image_accepted(true)
                            .introduce((String) info.get(18))
                            .mbti((String) info.get(19))
                            .personality((String) info.get(20))
                            .religion((String) info.get(21))
                            .smoke((String) info.get(22))
                            .style((String) info.get(23))
                            .build()
            );


            System.out.println(senderDTO.getAccessToken());
        });
        return CommonApiResponse.of(true);
    }
}
