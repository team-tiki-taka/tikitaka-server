package com.tikitaka.naechinso.domain.member;

import com.tikitaka.naechinso.domain.member.dto.*;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.pending.dto.PendingFindResponseDTO;
import com.tikitaka.naechinso.domain.recommend.RecommendService;
import com.tikitaka.naechinso.global.annotation.AuthMember;
import com.tikitaka.naechinso.global.config.CommonApiResponse;
import com.tikitaka.naechinso.global.config.security.jwt.JwtTokenProvider;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenService;

    @GetMapping
    @ApiOperation(value = "유저 자신의 모든 정보를 가져온다 (AccessToken)")
    public CommonApiResponse<MemberCommonResponseDTO> getMyInformation(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(memberService.readCommonMember(member));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "고유 아이디의 유저를 가져온다 (AccessToken)")
    public CommonApiResponse<MemberCommonResponseDTO> getMemberById(
            @PathVariable("id") Long id
//            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(MemberCommonResponseDTO.of(memberService.findById(id)));
    }

//    @GetMapping("/{id}/profile")
//    @ApiOperation(value = "고유 아이디의 유저 프로필과 추천인 정보를 가져온다 (AccessToken)")
//    public CommonApiResponse<MemberOppositeProfileResponseDTO> getMemberProfileById(
//            @PathVariable("id") Long id,
//            @ApiIgnore @AuthMember Member member
//    ) {
//        return CommonApiResponse.of(memberService.readOppositeMemberDetailAndRecommendById(member, id));
//    }

    @GetMapping("/detail")
    @ApiOperation(value = "회원가입 세부 정보를 가져온다 (AccessToken)")
    public CommonApiResponse<MemberDetailResponseDTO> getMemberDetail(
            @ApiIgnore @AuthMember Member member
    ) {
        final MemberDetailResponseDTO res = memberService.readDetail(member);
        return CommonApiResponse.of(res);
    }

    @PostMapping("/login")
    @ApiOperation(value = "FCM Token 을 등록하여 로그인 처리를 진행한다 (AccessToken)")
    public CommonApiResponse<MemberLoginResponseDTO> login(
            @Valid @RequestBody MemberLoginRequestDTO requestDTO,
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(memberService.login(member, requestDTO));
    }

    @PostMapping("/login/force")
    @ApiOperation(value = "FCM Token 을 교체한다 (AccessToken)")
    public CommonApiResponse<MemberLoginResponseDTO> forceLogin(
            @Valid @RequestBody MemberLoginRequestDTO requestDTO,
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(memberService.forceLogin(member, requestDTO));
    }

    @PostMapping("/logout")
    @ApiOperation(value = "FCM Token 을 삭제하여 로그아웃 처리한다 (AccessToken)")
    public CommonApiResponse<MemberLoginResponseDTO> logout(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(memberService.logout(member));
    }

    @PostMapping("/join")
    @ApiOperation(value = "유저를 공통 정보로 가입시킨다 (RegisterToken)")
    public CommonApiResponse<MemberCommonJoinResponseDTO> createCommonMember(
            HttpServletRequest request,
            @Valid @RequestBody MemberCommonJoinRequestDTO dto
    ) {
        String phone = jwtTokenService.parsePhoneByRegisterToken(request);
        return CommonApiResponse.of(memberService.joinCommonMember(phone, dto));
    }

    @PostMapping("/join/detail")
    @ApiOperation(value = "회원가입 세부 정보를 입력하여 최종 가입시킨다 (AccessToken)")
    public CommonApiResponse<MemberDetailResponseDTO> createMemberDetail(
            @Valid @RequestBody MemberDetailJoinRequestDTO dto,
            @ApiIgnore @AuthMember Member member
    ) {
        final MemberDetailResponseDTO res = memberService.createDetail(member, dto);
        return CommonApiResponse.of(res);
    }

    @PatchMapping("/common")
    @ApiOperation(value = "유저를 공통 정보를 수정한다 (AccessToken)")
    public CommonApiResponse<MemberCommonJoinResponseDTO> updateCommonMember(
            @Valid @RequestBody MemberUpdateCommonRequestDTO dto,
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(memberService.updateCommonMember(member, dto));
    }

    @PatchMapping("/job")
    @ApiOperation(value = "직업 인증 정보 업데이트 요청을 보낸다 (AccessToken)")
    public CommonApiResponse<MemberCommonResponseDTO> updateJob(
            @Valid @RequestBody MemberUpdateJobRequestDTO dto,
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(memberService.updateJobRequest(member, dto));
    }

    @PatchMapping("/edu")
    @ApiOperation(value = "학력 정보 업데이트 요청을 보낸다 (AccessToken)")
    public CommonApiResponse<MemberCommonResponseDTO> updateEdu(
            @Valid @RequestBody MemberUpdateEduRequestDTO dto,
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(memberService.updateEduRequest(member, dto));
    }

    @PatchMapping("/image")
    @ApiOperation(value = "프로필 이미지를 업데이트 한다 (AccessToken)")
    public CommonApiResponse<MemberDetailResponseDTO> updateImage(
            @Valid @RequestBody MemberUpdateImageRequestDTO dto,
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(memberService.updateImage(member, dto));
    }

    @PatchMapping("/accept")
    @ApiOperation(value = "동의 정보 여부를 업데이트 한다 (AccessToken)")
    public CommonApiResponse<MemberAcceptsResponseDTO> updateAccepts(
            @Valid @RequestBody MemberUpdateAcceptsRequestDTO dto,
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(memberService.updateAccepts(member, dto));
    }


    //페이징 처리 추가할 예정
    @GetMapping("/find")
    @ApiOperation(value = "[Admin]현재 가입한 모든 유저를 불러온다 (AccessToken)")
    public CommonApiResponse<List<MemberFindResponseDTO>> getMyInformation(
//            @RequestBody RecommendMemberAcceptRequestDTO dto,
//            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(memberService.findAll());
    }
}
