package com.tikitaka.naechinso.domain;

import com.tikitaka.naechinso.domain.card.CardRepository;
import com.tikitaka.naechinso.domain.card.CardService;
import com.tikitaka.naechinso.domain.card.entity.Card;
import com.tikitaka.naechinso.domain.match.MatchRepository;
import com.tikitaka.naechinso.domain.match.MatchService;
import com.tikitaka.naechinso.domain.match.constant.MatchStatus;
import com.tikitaka.naechinso.domain.match.dto.MatchResponseDTO;
import com.tikitaka.naechinso.domain.match.entity.Match;
import com.tikitaka.naechinso.domain.member.MemberDetailRepository;
import com.tikitaka.naechinso.domain.member.MemberRepository;
import com.tikitaka.naechinso.domain.member.MemberService;
import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.dto.*;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.member.entity.MemberDetail;
import com.tikitaka.naechinso.domain.pending.PendingRepository;
import com.tikitaka.naechinso.domain.pending.PendingService;
import com.tikitaka.naechinso.domain.point.PointRepository;
import com.tikitaka.naechinso.domain.recommend.RecommendRepository;
import com.tikitaka.naechinso.domain.recommend.RecommendService;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendBySenderRequestDTO;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendResponseDTO;
import com.tikitaka.naechinso.global.annotation.AuthMember;
import com.tikitaka.naechinso.global.config.CommonApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

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
    private final PendingRepository pendingRepository;

    private final CardService cardService;
    private final CardRepository cardRepository;
    private final MatchService matchService;
    private final MatchRepository matchRepository;
    private final PointRepository pointRepository;


    @GetMapping("/create-recommend-request-member")
    @ApiOperation(value = "[*TEST*] 추천사를 요청하는 유저를 생성하고 엑세스 토큰을 반환한다")
    public CommonApiResponse<Object> generateRecommendRequestUser(
    ) {
        MemberCommonJoinResponseDTO responseDTO = memberService.joinCommonMember(
                "01012345678",
                MemberCommonJoinRequestDTO.builder()
                        .age(1998)
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

    @GetMapping("/create-recommend-received-member")
    @ApiOperation(value = "[*TEST*] 추천사를 받은 멤버를 임시 유저로 가입시킨 후 엑세스 토큰을 반환한다")
    public CommonApiResponse<Object> generateRecommendReceiver(
    ) {
        List<Object> senderMemberInfo = Arrays.asList("01010101010", 1998, Gender.M, "노경닉", "edu-image-admin.jpg", "홍익", "대학교", "컴퓨터공학과", "1~3년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 180, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인, 상냥한, 섬세한", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"); //추천인
        Member sender = memberService.findByPhone(memberService.joinCommonMember(
                (String) senderMemberInfo.get(0),
                MemberCommonJoinRequestDTO.builder()
                        .age((int) senderMemberInfo.get(1))
                        .gender((Gender) senderMemberInfo.get(2))
                        .name((String) senderMemberInfo.get(3))
                        .acceptsInfo(true)
                        .acceptsLocation(true)
                        .acceptsMarketing(true)
                        .acceptsReligion(true)
                        .acceptsService(true)
                        .build()).getPhone());
        sender.updateEdu(MemberUpdateEduRequestDTO.builder()
                .eduImage((String) senderMemberInfo.get(4))
                .eduName((String) senderMemberInfo.get(5))
                .eduLevel((String) senderMemberInfo.get(6))
                .eduMajor((String) senderMemberInfo.get(7))
                .build());
        memberRepository.save(sender);
        memberRepository.flush();


        List<Object> receiverInfo = Arrays.asList("01012345678", 1998, Gender.W, "진하수", "edu-image020.jpg", "연세", "대학교", "기계공학과", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 163, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어");
        //receiver
        MemberCommonJoinResponseDTO receiverDTO = memberService.joinCommonMember(
                (String) receiverInfo.get(0),
                MemberCommonJoinRequestDTO.builder()
                        .age((int) receiverInfo.get(1))
                        .gender((Gender) receiverInfo.get(2))
                        .name((String) receiverInfo.get(3))
                        .acceptsInfo(true)
                        .acceptsLocation(true)
                        .acceptsMarketing(true)
                        .acceptsReligion(true)
                        .acceptsService(true)
                        .build());

        RecommendResponseDTO recommend = recommendService.createRecommend(sender, RecommendBySenderRequestDTO
                .builder()
                .phone((String) receiverInfo.get(0))
                .age((int) receiverInfo.get(1))
                .gender((Gender) receiverInfo.get(2))
                .name((String) receiverInfo.get(3))
                .period((String) receiverInfo.get(8))
                .meet((String) receiverInfo.get(9))
                .appeals(List.of(StringUtils.split((String) receiverInfo.get(11), ",")))
                .appealDetail((String) receiverInfo.get(12))
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
                        .age(1998)
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

//    @GetMapping("/create-cards-each")
//    @ApiOperation(value = "[*TEST*] 생성한 두 유저의 카드를 각각 생성한다")
//    public CommonApiResponse<Object> createCardsEach(
//    ) {
//        Member member1 = memberService.findByPhone("01011111111");
//        Member member2 = memberService.findByPhone("01012345678");
//
//        cardService.createCard(member1);
//        cardService.createCard(member1);
//        cardService.createCard(member2);
//        cardService.createCard(member2);
//
//        return CommonApiResponse.of(true);
//
//    }


    @GetMapping("/create-job-pending-and-recommend-other")
    @ApiOperation(value = "[*TEST*] 직업 업데이트 요청하는 멤버를 생성하고 유저 한명을 추천한 뒤 다른 유저의 엑세스 토큰을 반환한다")
    public CommonApiResponse<Object> createJobPendingMemberAndRecommendOther(
    ) {
        MemberCommonJoinResponseDTO senderDTO = memberService.joinCommonMember(
                "01012345678",
                MemberCommonJoinRequestDTO.builder()
                        .age(1998)
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
                        .age(1998)
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
                .age(1998)
                .period("1년")
                .appeals(List.of("[패션센스 \uD83E\uDDE5", "사랑꾼 \uD83D\uDC97", "애교쟁이 \uD83D\uDE18"))
                .phone("01011111111")
                .meet("CMC에서")
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
    @ApiOperation(value = "[*TEST*] 정회원으로 가입한 유저를 둘 생성하고 그 중 첫 번째 유저의 엑세스 토큰을 반환한다")
    public CommonApiResponse<Object> createTwoRegularMember() {

        MemberCommonJoinResponseDTO senderDTO = memberService.joinCommonMember(
                "01012345678",
                MemberCommonJoinRequestDTO.builder()
                        .age(1998)
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
                        .age(1995)
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
                .age(1998)
                .period("3달")
                .appeals(List.of("[패션센스 \uD83E\uDDE5", "사랑꾼 \uD83D\uDC97", "애교쟁이 \uD83D\uDE18"))
                .appealDetail("짱짱짱 멋짐")
                .phone("01012345678")
                .meet("테스트중에")
                .gender(Gender.M)
                .name("박스")
                .build());
        List<RecommendResponseDTO> recommend1 = recommendService.findAllBySenderPhone(member2.getPhone());

        recommendService.createRecommend(member1, RecommendBySenderRequestDTO
                .builder()
                .age(1998)
                .period("1년")
                .appeals(List.of("[패션센스 \uD83E\uDDE5", "사랑꾼 \uD83D\uDC97", "애교쟁이 \uD83D\uDE18"))
                .appealDetail("짱짱짱 멋짐")
                .phone("01011111111")
                .meet("CMC에서")
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
                        .personalities("패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18")
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
                        .personalities("패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18")
                        .religion("무교")
                        .smoke("비흡연자")
                        .style("개굿")
                        .build()
        );


        return CommonApiResponse.of(senderDTO);
    }

    @GetMapping("/create-multiple-users")
    @ApiOperation(value = "[*TEST*] 정회원으로 가입한 유저를 남녀 10명씩 생성하고, 그 중 마지막 여성 멤버 하나의 엑세스 토큰 반환")
    public CommonApiResponse<Object> createMultipleMembers() {
        List<Object> adminMemberInfo = Arrays.asList("01012345678", 1998, Gender.M, "노경닉", "edu-image-admin.jpg", "홍익", "대학교", "컴퓨터공학과", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 180, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"); //어드민 겸 추천인

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
                Arrays.asList("01011111111", 1998, Gender.M, "허시준", "edu-image001.jpg", "서강", "대학교", "컴퓨터공학과", "1~3년", "CMC에서 만남", "착함", "\"패션센스 \uD83E\uDDE5\", \"사랑꾼 \uD83D\uDC97\", \"애교쟁이 \uD83D\uDE18\"", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 180, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01012222222", 1997, Gender.M, "민성진", "edu-image002.jpg", "한국", "고등학교", "자동차정비", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 185, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "INFP", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01013333333", 1996, Gender.M, "김상혁", "edu-image003.jpg", "서울", "대학원", "인공지능공학", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 182, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "기독교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01014444444", 1995, Gender.M, "권영성", "edu-image004.jpg", "홍익", "대학교", "시각디자인학과", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 183, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "INFJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "불교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01015555555", 1994, Gender.M, "배규빈", "edu-image005.jpg", "한성", "고등학교", "상업", "3~5년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 178, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01016666666", 1993, Gender.M, "권민기", "edu-image006.jpg", "서울", "고등학교", "디자인", "3~5년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 172, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESFJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01017777777", 1992, Gender.M, "김민성", "edu-image007.jpg", "연세", "대학원", "영어영문학과", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 173, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ENFP", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "기독교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01018888888", 1991, Gender.M, "임정혁", "edu-image008.jpg", "연세", "대학교", "전기전자공학과", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 175, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01019999999", 1990, Gender.M, "성재오", "edu-image009.jpg", "이대부속", "고등학교", "공학", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 168, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01010000000", 1989, Gender.M, "차재훈", "edu-image010.jpg", "국민", "대학교", "미디어학과", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 176, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTP", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),

                Arrays.asList("01022200000", 1998, Gender.W, "민장효", "edu-image011.jpg", "서강", "대학원", "물류학과", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 160, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "INFP", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01022211111", 1998, Gender.W, "김민서", "edu-image012.jpg", "홍익", "대학교", "조소과", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 162, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01022222222", 1998, Gender.W, "노혜지", "edu-image013.jpg", "한국", "고등학교", "디자인", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 165, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ENFP", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "기독교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01022233333", 1998, Gender.W, "류라해", "edu-image014.jpg", "이화여자", "대학교", "사이버보안학과", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 155, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "기독교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01022244444", 1998, Gender.W, "민예지", "edu-image015.jpg", "이화여자", "대학교", "국어국문학과", "3~5년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 150, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "INFJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01022255555", 1998, Gender.W, "류유주", "edu-image016.jpg", "한국", "고등학교", "공학", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 174, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "INTP", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "기독교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01022266666", 1998, Gender.W, "임혜서", "edu-image017.jpg", "서울", "대학원", "법학과", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 172, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "불교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01022277777", 1998, Gender.W, "임한하", "edu-image018.jpg", "서울", "대학교", "의상학과", "3~5년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 168, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ENTP", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01022288888", 1998, Gender.W, "권민영", "edu-image019.jpg", "고려", "대학교", "의학", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 175, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESFJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01022299999", 1998, Gender.W, "진하수", "edu-image020.jpg", "연세", "대학교", "기계공학과", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 163, "내친소 사용하기", "profile-001-01.png,profile-001-02.png,profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어")
        );

        String accessToken = null;

        for (List<Object> info : joinRequestList) {
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
                    .appeals(List.of(StringUtils.split((String) info.get(11), ",")))
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
                            .personalities((String) info.get(20))
                            .religion((String) info.get(21))
                            .smoke((String) info.get(22))
                            .style((String) info.get(23))
                            .build()
            );

           accessToken = senderDTO.getAccessToken();
        }
        return CommonApiResponse.of(accessToken);
    }

    @GetMapping("/create-match")
    @ApiOperation(value = "[*TEST*] 나에게 호감을 보낸 매칭을 하나 생성한다")
    public CommonApiResponse<Object> createMatch(
            @ApiIgnore @AuthMember Member member
    ) {
        Match match = Match.builder()
                .fromMember(memberService.findById(20L))
                .toMember(memberService.findByMember(member))
                .status(MatchStatus.PENDING)
                .build();

        matchRepository.save(match);

        return CommonApiResponse.of(MatchResponseDTO.of(match));
    }

    @GetMapping("/create-match-in-row")
    @ApiOperation(value = "[*TEST*] accessToken 의 유저가 소유한 매칭 정보를 10 개 무작위 생성한다")
    public CommonApiResponse<Object> createCardInRowEight(
            @ApiIgnore @AuthMember Member member
    ) {
        Member findMember = memberService.findByMember(member);
        List<Match> matchList = List.of(
                Match.builder().fromMember(findMember).toMember(findMember).isExpired(false).status(MatchStatus.ACCEPTED).build(),
                Match.builder().fromMember(findMember).toMember(findMember).isExpired(false).status(MatchStatus.OPEN).build(),
                Match.builder().fromMember(findMember).toMember(findMember).isExpired(false).status(MatchStatus.OPEN).build(),
                Match.builder().fromMember(findMember).toMember(findMember).isExpired(false).status(MatchStatus.ACCEPTED).build(),
                Match.builder().fromMember(findMember).toMember(findMember).isExpired(false).status(MatchStatus.PENDING).build(),
                Match.builder().fromMember(findMember).toMember(findMember).isExpired(false).status(MatchStatus.PENDING).build(),
                Match.builder().fromMember(findMember).toMember(findMember).isExpired(false).status(MatchStatus.PENDING).build(),
                Match.builder().fromMember(findMember).toMember(findMember).isExpired(false).status(MatchStatus.PENDING).build(),
                Match.builder().fromMember(findMember).toMember(findMember).isExpired(false).status(MatchStatus.PENDING).build(),
                Match.builder().fromMember(findMember).toMember(findMember).isExpired(false).status(MatchStatus.PENDING).build()
        );

        matchRepository.saveAllAndFlush(matchList);
        return CommonApiResponse.of(matchList);
    }

    @GetMapping("/test-create-users")
    @ApiOperation(value = "[*TEST*] 시연 영상을 위한 남자 회원의 레코드 생성 및 엑세스 토큰 반환")
    public CommonApiResponse<Object> createShowTestRecord() {
        List<Object> recommenderInfo = Arrays.asList(
                "01098765432",
                1998,
                Gender.W,
                "김제은",
                "test-job-001.jpg",
                "당근마켓",
                "서비스기획자",
                "서울시 서초구",
                "1~3년",
                "CMC에서 만남",
                "착함",
                "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18",
                "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야",
                "서울시 마포구",
                "1병",
                180,
                "내친소 사용하기",
                "test_photo_001.png,test_photo_002.png,test_photo_003.png",
                "반갑습니다 내친소 여러분!",
                "ESTJ",
                "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️",
                "무교",
                "비흡연자",
                "다양한 경험과 일상의 대화를 함께하는 연애"
        ); //추천인

        Member recommender = memberService.findByPhone(memberService.joinCommonMember(
                (String) recommenderInfo.get(0),
                MemberCommonJoinRequestDTO.builder()
                        .age((int) recommenderInfo.get(1))
                        .gender((Gender) recommenderInfo.get(2))
                        .name((String) recommenderInfo.get(3))
                        .acceptsInfo(true)
                        .acceptsLocation(true)
                        .acceptsMarketing(true)
                        .acceptsReligion(true)
                        .acceptsService(true)
                        .build()).getPhone());
        recommender.updateJob(MemberUpdateJobRequestDTO.builder()
                .jobImage((String) recommenderInfo.get(4))
                .jobName((String) recommenderInfo.get(5))
                .jobPart((String) recommenderInfo.get(6))
                .jobLocation((String) recommenderInfo.get(7))
                .build());
        memberRepository.save(recommender);

        final List<List<Object>> memberList = Arrays.asList(
                Arrays.asList(
                        "01033333333",
                        1993,
                        Gender.M,
                        "김범수",
                        "test_job_002.jpg",
                        "삼성전자",
                        "서버 개발자",
                        "서울시 서초구",
                        "1~3년",
                        "CMC에서 만났어",
                        "상대방에게 공감해주고 또 챙겨주는걸 잘 해!",
                        "\"패션센스 \uD83E\uDDE5\", \"사랑꾼 \uD83D\uDC97\", \"애교쟁이 \uD83D\uDE18\"",
                        "이 오빠는 진짜 이런 사람이 존재한다고? 싶을 정도로 진짜 상상속에서 튀어나온 사람 유형이었어! 예를들어 나랑 사이드 프로젝트를 하면서 만났는데 남자 여자 생각의 차이가 날 만한 그런 문제에서도 오히려 여자랑 생각이 더 비슷한 느낌이라고 하면 이해가 되려나..? 배려심이 엄청 넘치고 그래서 생각이 다른 사람에 비해서 훨 깊다는 것을 느낀적이 많아! 그리고 얼굴도 잘생기고 목소리도 좋아. 아 그리고 요리도 진짜 잘해 ㅋㅋㅋ 하나 에피소드가 우리집 제사지낸다고 했더니 자기네 집은 명절때 제사 안지내는데 동생이랑 명절 분위기 내고 싶어서 전 굽는대… 나한테는 약간 신선한 충격이었던 에피소드ㅋㅋㅋ",
                        "서울시 마포구",
                        "1병",
                        180,
                        "테니스, 요리",
                        "test_photo_001.png,test_photo_002.png,test_photo_003.png",
                        "반갑습니다 내친소 여러분!",
                        "ESTJ",
                        "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️",
                        "무교",
                        "비흡연자",
                        "다양한 경험과 일상의 대화를 함께하는 연애")
        );

        String accessToken = null;

        for (List<Object> info : memberList) {
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

            member.updateJob(MemberUpdateJobRequestDTO.builder()
                    .jobImage((String) recommenderInfo.get(4))
                    .jobName((String) recommenderInfo.get(5))
                    .jobPart((String) recommenderInfo.get(6))
                    .jobLocation((String) recommenderInfo.get(7))
                    .build());

            memberRepository.save(member);
            memberRepository.flush();

            recommendService.createRecommend(recommender, RecommendBySenderRequestDTO
                    .builder()
                    .phone((String) info.get(0))
                    .age((int) info.get(1))
                    .gender((Gender) info.get(2))
                    .name((String) info.get(3))
                    .period((String) info.get(8))
                    .meet((String) info.get(9))
                    .appeals(List.of(StringUtils.split((String) info.get(11), ",")))
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
                            .personalities((String) info.get(20))
                            .religion((String) info.get(21))
                            .smoke((String) info.get(22))
                            .style((String) info.get(23))
                            .build()
            );

            accessToken = senderDTO.getAccessToken();
        }
        return CommonApiResponse.of(accessToken);
    }


    @DeleteMapping("/drop-all-table")
    @ApiOperation(value = "[*TEST*] 모든 DB 테이블을 초기화")
    public CommonApiResponse<Object> dropAllTable(){
        matchRepository.deleteAll();
        pointRepository.deleteAll();
        pendingRepository.deleteAll();
        recommendRepository.deleteAll();
        cardRepository.deleteAll();
        memberDetailRepository.deleteAll();
        memberRepository.deleteAll();
        return CommonApiResponse.of(true);
    }
}
