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
        List<Object> senderMemberInfo = Arrays.asList("01010101010", 1998, Gender.M, "노경닉", "edu-image-admin.jpg", "홍익", "대학교", "컴퓨터공학과", "1~3년", "CMC에서 만남", "착함", "자기관리, 귀여워, 차분해", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 180, "내친소 사용하기", "member/profile-001-01.png,member/profile-001-02.png,member/profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인, 상냥한, 섬세한", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"); //추천인
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


        List<Object> receiverInfo = Arrays.asList("01012345678", 1998, Gender.W, "진하수", "edu-image020.jpg", "연세", "대학교", "기계공학과", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 163, "내친소 사용하기", "member/profile-001-01.png,member/profile-001-02.png,member/profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어");
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
    @ApiOperation(value = "[*TEST*] 추천인 6명 그리고 정회원으로 가입한 유저를 남녀 8명씩 생성하고, 그 중 마지막 여성 멤버 하나의 엑세스 토큰 반환")
    public CommonApiResponse<Object> createMultipleMembers() {

        List<Member> recommenderMemberList = new ArrayList<>();
        final List<List<Object>> recommenderList = Arrays.asList(
                Arrays.asList("01000000001", 1998, Gender.M, "조성민", "edu-image001.jpg", "포항공과", "대학교", "전자공학과", "1~3년", "CMC에서 만남", "착함", "\"패션센스 \uD83E\uDDE5\", \"사랑꾼 \uD83D\uDC97\", \"애교쟁이 \uD83D\uDE18\"", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 180, "내친소 사용하기", "member/profile-001-01.png,member/profile-001-02.png,member/profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01000000002", 1997, Gender.M, "차지훈", "edu-image002.jpg", "경희", "대학교", "산업경영공학과", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 185, "내친소 사용하기", "member/profile-001-01.png,member/profile-001-02.png,member/profile-001-03.png", "반갑습니다 내친소 여러분!", "INFP", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01000000003", 1994, Gender.M, "박민준", "job-image005.jpg", "구글코리아", "서버 엔지니어", "서울시 강남구", "3~5년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 178, "내친소 사용하기", "member/profile-001-01.png,member/profile-001-02.png,member/profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01000000004", 1993, Gender.W, "김채원", "edu-image006.jpg", "김앤장", "프로모션", "서울시 서초구", "3~5년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 172, "내친소 사용하기", "member/profile-001-01.png,member/profile-001-02.png,member/profile-001-03.png", "반갑습니다 내친소 여러분!", "ESFJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01000000005", 1992, Gender.W, "노민경", "edu-image007.jpg", "AWS", "서포트 엔지니어링", "영어영문학과", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 173, "내친소 사용하기", "member/profile-001-01.png,member/profile-001-02.png,member/profile-001-03.png", "반갑습니다 내친소 여러분!", "ENFP", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "기독교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01000000016", 1989, Gender.W, "박정민", "edu-image010.jpg", "이화여자", "대학교", "디자인학부", "1~3년", "CMC에서 만남", "착함", "패션센스 \uD83E\uDDE5,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "자기 일을 진짜 책임감 있게 잘하고 주변을 늘 먼저 생각하는 친구야", "서울시 마포구", "1병", 176, "내친소 사용하기", "member/profile-001-01.png,member/profile-001-02.png,member/profile-001-03.png", "반갑습니다 내친소 여러분!", "ESTP", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어")
        );

        for (List<Object> info : recommenderList) {
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

            if (((String) info.get(6)).contains("학교")) {
                member.updateEdu(MemberUpdateEduRequestDTO.builder()
                        .eduImage((String) info.get(4))
                        .eduName((String) info.get(5))
                        .eduLevel((String) info.get(6))
                        .eduMajor((String) info.get(7))
                        .build());
            } else {
                member.updateJob(MemberUpdateJobRequestDTO.builder()
                        .jobImage((String) info.get(4))
                        .jobName((String) info.get(5))
                        .jobPart((String) info.get(6))
                        .jobLocation((String) info.get(7))
                        .build());
            }
            memberRepository.save(member);
            recommenderMemberList.add(member);
        }
        memberRepository.flush();


        final List<List<Object>> joinRequestList = Arrays.asList(
                Arrays.asList("01010000001", 1998, Gender.M, "허시준", "edu-image001.jpg", "롯데정보통신", "개발 및 인프라", "컴퓨터공학과", "3~5년", "대학교 친구", "착함", "\"패션센스 \uD83E\uDDE5\", \"사랑꾼 \uD83D\uDC97\", \"애교쟁이 \uD83D\uDE18\"", "너무 추천하고 싶은 사람이라 내 지인 중 1번으로 신청했어. 정말 매력있는 동생이야. 같이 일해본 적은 없지만 대학시절부터 항상 리더 포지션이었어. 사람들이 많이 따르는 친구야. 진정성 있고, 그룹 내에서 인정을 받은 편이야. 잘생겼고, 자기관리도 꾸준히 하는 친구야. 유머러스한 면도 있어서 만나면 어색하지 않게 친구가 잘 리드해줄거야! 만나보면 후회없을 친구! 너무 추천!", "서울시 강남구", "1병", 180, "악기 연주", "member/profile-001-01.png,member/profile-001-02.png,member/profile-001-03.png", "다양한 분야에 관심이 많아서 대화코드가 잘 맞을거야! 그리고 무엇보다 리액션이 좋고 잘 웃는다는 소리를 많이 들어:)", "ESTJ", "섬세한 \uD83E\uDD32\uD83C\uDFFB,지적인 \uD83E\uDDD0,감성적인 \uD83D\uDCAB", "무교", "비흡연자", "서로 같이 있으면 힐링이 되고 같이 문제를 해결해나갈 수 있는 연애를 하고 싶어! 같이 맛있는 거에 맛있는 술 마시면서 힐링"),
                Arrays.asList("01010000002", 1997, Gender.M, "민성진", "edu-image002.jpg", "AWS", "서포트 엔지니어", "서울시 강남구", "5년 이상", "대학교 친구", "착함", "유머러스 \uD83D\uDE1C,사랑꾼 \uD83D\uDC97,\uD83C\uDF6F성대", "대학 동기인 이 친구는 솔직하고 자신만의 가치관이 뚜렷한 친구라 항상 응원하고 있어! 대학 들어가서 가장 친하게 지낸 친구인만큼 이 친구에 대해 잘 알고있다고 생각하는데 정말 만나봐서 후회없을 사람이야. 어떤 일이든 최선을 다하고 유머러스한 친구라 멋지다고 생각해. 사진은 별로인데 학교 홍보대사도 했을만큼 외모도 괜찮아. 얼굴은 유연석을 진짜 억울하게 닮아서 내가 이 친구 때문에 미스터션샤인을 못 봤어. 이 친구의 연애 스타일은 편안함을 추구하는 것 같아. 편안하게 한번 만나보면 좋을 것 같아!", "서울시 관악구", "1병", 185, "게임", "member/profile-002-01.png,member/profile-002-02.png,member/profile-002-03.png", "나 옷 잘 입어!! 취향 맞춰서 코디할 수 있지", "INFP", "열정적인 \uD83D\uDD25,낙천적인 \uD83D\uDE07,상냥한 ☺️", "무교", "비흡연자", "서로 많은 대화를 하면서 그때그때의 감정을 공유할 수 있는 연애를 하고 싶습니다 ㅎㅎ"),
                Arrays.asList("01010000003", 1996, Gender.M, "김상혁", "edu-image003.jpg", "서강", "대학교", "경영&빅데이터 사이언스", "1~3년", "대학교 친구", "착함", "패션센스 \uD83E\uDDE5,자기관리 \uD83C\uDFCA\uD83C\uDFFB\u200D♀️,일잘러 \uD83E\uDD13", "창업동아리에서 만난 친한 동생이야. 이 친구는 내가 아는 사람들 중에 가장 유머러스한 친구 중 하나야. 같이 있으면 정말 즐겁고 시간이 빨리간다고 해야할까. 또 별명이 봉무위키였던 적이 있을 정도로 박학다식해. 아는게 정말 많고 세상에 대해 호기심이 많아서 매사에 열심히 사는 친구라서 정말 멋진 친구라고 생각해!", "서울시 금천구", "1병", 182, "넷플릭스", "member/profile-003-01.png,member/profile-003-02.png,member/profile-003-03.png", "먹스타그램 운영자! 먹잘알에 옷 좋아해요:)", "ESTJ", "자유로운 \uD83C\uDD93,지적인 \uD83E\uDDD0,듬직한 \uD83D\uDCAA\uD83C\uDFFB️", "기독교", "비흡연자", "서로의 시간을 존중하는 성숙한 연애를 하고 싶어!!"),
                Arrays.asList("01010000004", 1995, Gender.M, "김민성", "edu-image004.jpg", "신한은행", "리스크 관리", "서울시 종로구", "1년 이하", "회사 친구", "착함", "자기관리 \uD83C\uDFCA\uD83C\uDFFB\u200D♀️,섬세해 \uD83E\uDEA1,애교쟁이 \uD83D\uDE18", "매우 잘생겼고 인성이 바르고 매우 다정한 성격입니다. 재주도 많고 매우 똑똑하며 가끔 유재석같은 재치를 보이면서도 이재용같은 멀끔함도 있습니다.", "경기도 파주시", "1병", 183, "유튜브", "member/profile-004-01.png,member/profile-004-02.png,member/profile-004-03.png", "공감, 배려 정말 잘해 / 리액션이 정말 좋고 잘 웃어", "INFJ", "열정적인 \uD83D\uDD25,4차원인 \uD83D\uDC7D,상냥한 ☺️", "불교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01010000005", 1994, Gender.M, "배규빈", "edu-image005.jpg", "초등학교", "교사", "서울시 강남구", "1년 이하", "회사 친구", "착함", "섬세해 \uD83E\uDEA1,일잘러 \uD83E\uDD13,애교쟁이 \uD83D\uDE18", "제가 일을 하며 만나게 된 동생입니다. 처음에는 그냥 그랬는데, 알고지낼 수록 이 친구의 배려심과 섬세함에 감탄하고 있어요. 누군가 힘든 일이 있을 때 좋은 일이 있을 때 진심으로 함께 슬퍼해주고 또 응원해주는 친구랍니다. 고등학교 때부터 개발을 하고 현재는 외주개발사 대표로 있어요. 어린 나이임에도 불구하고 많은 직원을 거느린 실력있는 대표랍니다!", "경기도 성남시", "1병", 178, "운동", "member/profile-005-01.png,member/profile-005-02.png,member/profile-005-03.png", "고민, 걱정을 잘 들어주고 감정기복이 거의 없어서 안정감이 든다는 이야기를 많이 들어", "ESTJ", "듬직한 \uD83D\uDCAA\uD83C\uDFFB,지적인 \uD83E\uDDD0,감성적인 \uD83D\uDCAB️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01010000006", 1993, Gender.M, "권민기", "edu-image006.jpg", "네이버", "서버 개발자", "경기도 성남시", "5년 이상", "초/중/고 친구", "착함", "패션센스 \uD83E\uDDE5,화목한 가정 \uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC66,유머러스 \uD83D\uDE1C", "동네친구이자 공부메이트였던 민수는 한번 시작한일에 대해 끝까지 하는 책임감이 있고 뇌섹남임! 또 유머러스해서 대인관계도 좋고 친구들 사이에서 분위기메이커야! 또 운동도 열심히하고 옷을 잘입어서 스타일이 좋아 :) 노래도 엄청 잘함 :) 지금은 로스쿨을 준비하고 있고 다재다능한 친구를 원하고 뇌섹남이자 유머러스한 남자를 원한다면 강추!", "서울시 마포구", "1병", 172, "운동", "member/profile-006-01.png,member/profile-006-02.png,member/profile-006-03.png", "상대방 이야기를 잘 들어줘!", "ESFJ", "열정적인 \uD83D\uDD25,섬세한 \uD83E\uDD32\uD83C\uDFFB,상냥한 ☺️", "무교", "비흡연자", "같은 취미를 공유하고 같이 미래를 그려볼 수 있는 연애"),
                Arrays.asList("01010000007", 1992, Gender.M, "김민성", "edu-image007.jpg", "로펌", "변호사", "서울시 서초구", "1년 이하", "회사 친구", "착함", "귀여워 \uD83D\uDC39,다정다감 \uD83D\uDCAA\uD83C\uDFFB,차분해 \uD83C\uDF75", "회사 인턴하면서 친해진 형이야! 물론 나랑 같은 학교라 졸업식 때 사진도 같이 찍었어 ㅎㅎ 일단 엄청 다정하고 착해. 보통 남자 친구끼리 욕도 좀 하고 거칠게 장난도 치잖아? 근데 이 형은 그런 면이 없고 오히려 나 포함 동생들이 장난쳐도 허허하면서 웃는 형이야 (리액션이 좋아서 계속 장난치고 싶어). 운동도 꾸준히 열심히 해. 운동 인증샷 몇 개월동안 연속으로 보고 있는 중.. 그리고 대화하다보면 상대방을 배려하고 이해해주는 편이라서 같이 있으면 편한 형이야! 좋은 상대 만나면 이쁜 연애 할 것 같아서 추천~~", "서울시 종로구", "1병", 173, "낚시", "member/profile-007-01.png,member/profile-007-02.png,member/profile-007-03.png", "친해지면 재밌어! 얘기하는 것도 좋아하구 함께 시간 보내는걸 좋아해", "ENFP", "열정적인 \uD83D\uDD25,듬직한 \uD83D\uDCAA\uD83C\uDFFB,낙천적인 \uD83D\uDE07️", "기독교", "비흡연자", "맛집도 다니고 같이 쇼핑도 가고 드라이브도 같이 갈 수 있는 재미있는 연애!"),
                Arrays.asList("01010000008", 1991, Gender.M, "임정혁", "edu-image008.jpg", "퍼시스그룹", "경영기획", "서울시 강남구", "1~3년", "대학교 친구", "착함", "패션센스 \uD83E\uDDE5,일잘러 \uD83E\uDD13,섬세해 \uD83E\uDEA1", "둘이서 2박3일 여행다녀온 적도 있었는데 굉장히 섬세하면서도 귀여운 친구야! 완벽한 INFJ상에 들어맞으면서도 사람만나는 걸 굉장히 좋아하구 인생을 즐기면서 사는 것 같아ㅎㅎ 빡센 증권사와서도 일도 잘하고 사람들도 잘 챙겨준다는 이야기를 주변사람들한테서 자주 전해들어!", "서울시 송파구", "1병", 175, "여행", "member/profile-008-01.png,member/profile-008-02.png,member/profile-008-03.png", "누구보다 긍정적이고 솔직해, 분위기 좋은 술집을 잘알아", "ESTJ", "4차원인 \uD83D\uDC7D,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "거주지가 가까웠음 좋겠어, 서로 관심분야가 비슷해서 할말이 많고 서로 배우는 연애를 하고싶어"),

                Arrays.asList("01020000000", 1998, Gender.W, "강수현", "edu-image011.jpg", "이화여자", "대학교", "사이버보안전공", "1년 이하", "회사 친구", "착함", "다정다감 \uD83D\uDCAA\uD83C\uDFFB,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "내가 정말정말 좋아하는 동생을 소개할게!!! 우리 수현이는 나랑 알고 지낸 기간이 오래되진 않았지만, 정말 잘 맞고 착해서 급속도로 가까워진 친구야ㅎㅎ 우연한 기회에 친해지게 됐는데 처음 만나자마자 나를 너무 잘 받아주고 좋아해줘서 내가 달라붙게(?) 됐어! 어린데도 성숙하고~ 야무지고~ 똑부러져서 대화도 잘 통하고 같이 있으면 심심할 틈이 없어! 내가 위에서 선택한대로 너무너무 예쁘게 생기기도 해서 누구랑 사귀게 될지.. 어딜 둘러봐도 아까운 내 하나뿐인 동생이야", "서울시 관악구", "1병", 160, "여행", "member/profile-009-01.png,member/profile-009-02.png,member/profile-009-03.png", "상대방이 만난 이성친구 중에 가장 예쁘고 몸매좋은 사람은 아닐지라도 인생에서 가장 기억에 남는 사람이 될 자신은 있음. 모든 방면에서", "INFP", "섬세한 \uD83E\uDD32\uD83C\uDFFB,지적인 \uD83E\uDDD0,상냥한 ☺️", "무교", "비흡연자", "베스트프랜드 같이 가까우면서도 서로 존중하고 배려하는 연애를 하고 싶어"),
                Arrays.asList("01020000001", 1998, Gender.W, "김민서", "edu-image012.jpg", "포항공과", "대학교", "전자공학과", "1~3년", "대학교 친구", "착함", "\uD83D\uDE97가 있어,사랑꾼 \uD83D\uDC97,애교쟁이 \uD83D\uDE18", "항상 잘 챙겨주고 긍정적이고 내 이야기를 잘 들어줘. 마음도 넓어서 사소한 걸로 기분 나빠하지 않고 배려심도 많아. 놀 때는 누구보다 재미있게 놀고 일하고 자기관리할 때는 또 철저하게 해. 너무 좋은 친구야", "서울시 서초구", "1병", 162, "요가", "member/profile-010-01.png,member/profile-010-02.png,member/profile-010-03.png", "눈치있고 상대방한테 잘 맞춰줍니다", "ESTJ", "자유로운 \uD83C\uDD93,지적인 \uD83E\uDDD0,털털한 \uD83E\uDDD4\uD83C\uDFFB\u200D♀️️", "무교", "비흡연자", "성숙한 연애를 하고 싶어. 서로에게 시너지가 나는 편안하고 재밌는 연애!\uD83E\uDDE1"),
                Arrays.asList("01020000002", 1998, Gender.W, "노혜지", "edu-image013.jpg", "마켓컬리", "커머스", "서울시 서초구", "1~3년", "대학교 친구", "착함", "여유있지 \uD83D\uDCB0,화목한 가정 \uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC66,애교쟁이 \uD83D\uDE18", "내 대학동기인 이 친구는 애교가 많은 친구야! 고민을 잘 들어주는데 누구보다도 진지하게 잘 들어주고 그래서 항상 믿고 의지하게 되는 친구야! 자기 일도 열심히 하고 사람들에겐 배려 넘치는 내 친구가 좋은 사람을 만나면 좋겠어!", "경기도 광명시", "1병", 165, "피아노", "member/profile-011-01.png,member/profile-011-02.png,member/profile-011-03.png", "난 맛집을 많이 알아! 그리고 성격이 섬세한 편이라 편안한 대화를 잘 이끌어가!", "ENFP", "열정적인 \uD83D\uDD25,자유로운 \uD83C\uDD93,4차원인 \uD83D\uDC7D️", "기독교", "비흡연자", "언제든 서로의 편이 되어주고, 서로에게 기댈 수 있는 신뢰 있고 편안한 연애를 하고 싶어!"),
                Arrays.asList("01020000003", 1998, Gender.W, "류지원", "edu-image014.jpg", "김앤장", "프로모션", "서울시 강남구", "1년 이하", "회사 친구", "착함", "패션센스 \uD83E\uDDE5,섬세해 \uD83E\uDEA1,애교쟁이 \uD83D\uDE18", "인턴했던 전 회사를 통해서 알게된 내 친구는 항상 밝은 친구야! 사람을 정말 잘 챙기고 업무적으로도 항상 인정 받았던 여러 면모에서 훌륭한 친구야! 난 이 친구랑 만난 첫날 같은 자리에서 3-4시간 떠들면서 친해졌을만큼 사람 얘기도 잘 들어주고 사람을 어색하게 하지 않는 성격 좋은 친구야! 이런 내 친구를 소개하고 싶어!!", "서울시 강북구", "1병", 155, "드럼", "member/profile-012-01.png,member/profile-012-02.png,member/profile-012-03.png", "상대방을 존중하며 잘 챙겨주고, 무엇보다 맛있는 요리를 많이 해줄 수 있어요:)", "ESTJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,상냥한 ☺️", "기독교", "비흡연자", "나는 오래 만나도 안정적이고 평화로운 (?) 연애를 하고 싶어! 같이 대화를 할 때 즐겁고, 서로의 장점을 배우면서 아껴줄 수 있는 연애를 하고싶어."),
                Arrays.asList("01020000004", 1998, Gender.W, "민예지", "edu-image015.jpg", "미쓰비시은행", "백오피스", "서울시 강남구", "3~5년", "대학교 친구", "착함", "실물파 \uD83D\uDC40,사랑꾼 \uD83D\uDC97,자기관리 \uD83C\uDFCA\uD83C\uDFFB\u200D♀️", "잘 놀면서 자기 일은 확실히 하는 친구야! 대학와서 다른 친구들이랑 놀면서 친해진 친구인데, 분위기를 주도하고 유머감각도 뛰어난 친구라고 생각했어. 노는 모습만 봐서 진짜 잘 놀줄 안다고만 생각했는데, 일 하는 모습보니까 자기 일도 잘하고, 열정적이고 꾸준히 공부하는 모습이 반전 매력인 거 같아! 퇴근하고 운동도 하는데..자기관리 끝판왕..이런 내친구가 가을 겨울 따뜻하게 보냈으면 좋겠어서 소개하고 싶어!!", "서울시 서대문구", "1병", 150, "여행", "member/profile-013-01.png,member/profile-013-02.png,member/profile-013-03.png", "사교성이 좋은 편이고, 믿음직스러운 편...?", "INFJ", "열정적인 \uD83D\uDD25,지적인 \uD83E\uDDD0,섬세한 \uD83E\uDD32\uD83C\uDFFB️", "무교", "비흡연자", "같이 취미를 즐길 수 있는 연애를 하고 싶어"),
                Arrays.asList("01020000005", 1998, Gender.W, "박지원", "edu-image016.jpg", "피트니스 스타트업", "PM", "서울시 강서구", "1~3년", "대학교 친구", "착함", "자기관리 \uD83C\uDFCA\uD83C\uDFFB\u200D♀️,일잘러 \uD83E\uDD13,애교쟁이 \uD83D\uDE18", "새하얗고 작고 귀엽고 애교가 많아 그리고 우리가 봉사동아리에서 만난 만큼 성격도 좋고 남을 배려하는 마음도 커 ! 그리고 술을 먹지 않아도 술자리에서 텐션을 잘 맞출만큼 재밌는 친구야! 동아리 ot때 제일 예쁘고 눈에 띠는 귀여운 친구여서 먼저 친해지고싶어서 다가가서 친해진 친구야\uD83E\uDEF6\uD83C\uDFFB", "서울시 강서구", "1병", 174, "유튜브", "member/profile-014-01.png,member/profile-014-02.png,member/profile-014-03.png", "키가 큰 편이에요 그리고 인상 좋다는 말 많이 들어요", "INTP", "자유로운 \uD83C\uDD93,지적인 \uD83E\uDDD0,털털한 \uD83E\uDDD4\uD83C\uDFFB\u200D♀️️", "기독교", "비흡연자", "서로 많은 대화를 하면서 그때그때의 감정을 공유할 수 있는 연애를 하고 싶습니다 ㅎㅎ"),
                Arrays.asList("01020000006", 1998, Gender.W, "진하수", "edu-image017.jpg", "한국과학기술연구원", "연구원", "대구광역시", "1~3년", "대학교 친구", "착함", "유머러스 \uD83D\uDE1C,섬세해 \uD83E\uDEA1,애교쟁이 \uD83D\uDE18", "이 친구는 항상 끊임없이 도전하고 성취하는 멋진 친구예요! 똑부러지게 자신의 커리어를 개척해나가는 동시에 주변 사람들에게도 무척이나 다정하고 세심합니다. 연애에 있어서는 굉장히 진중하고 깊은 친구라고 생각해요. 이 친구를 만나신다면 서로에게 긍정적인 자극을 주고 함께 성장하는 관계로 거듭나실 수 있을거예요!", "서울시 마포구", "1병", 172, "운동", "member/profile-015-01.png,member/profile-015-02.png,member/profile-015-03.png", "잘 웃고 리액션을 잘하는 편이라 어색하지 않을거야!", "ESTJ", "열정적인 \uD83D\uDD25,자유로운 \uD83C\uDD93,감성적인 \uD83D\uDCAB️", "불교", "비흡연자", "같은 취미를 공유하고 같이 미래를 그려볼 수 있는 연애"),
                Arrays.asList("01020000007", 1998, Gender.W, "임한하", "edu-image018.jpg", "당근마켓", "서비스기획", "서울시 서초구", "5년 이상", "초/중/고 친구", "착함", "섬세해 \uD83E\uDEA1,사랑꾼 \uD83D\uDC97,일잘러 \uD83E\uDD13", "이 친구랑 만난게 중학교 1학년때니까.. 벌써 14년이 지났네..! 그냥 년수만 채운게 아니라 우린 일주일에 세번 이상 만나는 사이야 ㅋㅋ 그정도로 아주 가까운 사이라 난 이 친구의 거의 모든것을 알고있다고 할수있지. 그만큼 내친소에 내보내기 자신있다 이거야! 간단히 말하자면 늘씬하고 예쁜 외모에 공감능력 정말 좋고 심지어 너무 웃긴애야! 내가 이친구를 자주 만나는 이유가 있지 ㅎㅎ 힘든일이 생기면 가장 먼저 이 친구가 떠올라. 연애에서 깊은 대화, 마음의 교류가 중요하다고 생각한다면 이친구랑 잘맞을거야. 반면에 연애에 있어서는 각자 자기 할일 하고, 신뢰를 바탕으로 알아서 잘하자는 쿨한 타입이야! 부지런하고 일 잘하고 똑부러지는 내친구 제발 누가 데려가!! 진짜 좋은애야!", "서울시 마포구", "1병", 168, "테니스", "member/profile-016-01.png,member/profile-016-02.png,member/profile-016-03.png", "상대방 이야기를 잘 들어줘!", "ENTP", "열정적인 \uD83D\uDD25,털털한 \uD83E\uDDD4\uD83C\uDFFB\u200D♀️,상냥한 ☺️", "무교", "비흡연자", "맛집도 다니고 같이 쇼핑도 가고 드라이브도 같이 갈 수 있는 재미있는 연애!")
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

            if (((String) info.get(6)).contains("학교")) {
                member.updateEdu(MemberUpdateEduRequestDTO.builder()
                        .eduImage((String) info.get(4))
                        .eduName((String) info.get(5))
                        .eduLevel((String) info.get(6))
                        .eduMajor((String) info.get(7))
                        .build());
            } else {
                member.updateJob(MemberUpdateJobRequestDTO.builder()
                        .jobImage((String) info.get(4))
                        .jobName((String) info.get(5))
                        .jobPart((String) info.get(6))
                        .jobLocation((String) info.get(7))
                        .build());
            }

            memberRepository.save(member);
            memberRepository.flush();

            recommendService.createRecommend(
                    recommenderMemberList.get(new Random().nextInt(recommenderList.size())),
                    RecommendBySenderRequestDTO
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
