package com.tikitaka.naechinso.domain.member;

import com.tikitaka.naechinso.domain.member.dto.*;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.recommend.RecommendService;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendMemberAcceptRequestDTO;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendMemberAcceptWithUpdateJobRequestDTO;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendResponseDTO;
import com.tikitaka.naechinso.global.annotation.AuthMember;
import com.tikitaka.naechinso.global.config.CommonApiResponse;
import com.tikitaka.naechinso.global.config.security.jwt.JwtTokenProvider;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.UnauthorizedException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final RecommendService recommendService;
    private final JwtTokenProvider jwtTokenService;

    @GetMapping
    @ApiOperation(value = "유저 자신의 모든 정보를 가져온다 (AccessToken)")
    public CommonApiResponse<MemberCommonResponseDTO> getMyInformation(
            HttpServletRequest request,
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(MemberCommonResponseDTO.of(member));
    }

//    @PostMapping("/join")
//    @ApiOperation(value = "공통 유저를 기본 정보로 회원가입 시킨다 (registerToken 필요)")
//    public CommonApiResponse<MemberCommonResponseDTO> joinCommonMember(
//            HttpServletRequest request,
//            @Valid @RequestBody MemberCommonJoinRequestDTO dto)
//    {
//        String registerToken = request.getHeader("Authorization");
//        if (StringUtils.isBlank(registerToken) || !jwtTokenService.validateToken(registerToken)) {
//            throw new UnauthorizedException(ErrorCode.NO_TOKEN);
//        }
//
//        final MemberCommonResponseDTO res = memberService.createCommonMember(dto);
//        return CommonApiResponse.of(res);
//    }

    @GetMapping("/detail")
    @ApiOperation(value = "회원가입 세부 정보를 가져온다 (AccessToken 필요)")
    public CommonApiResponse<MemberDetailResponseDTO> getMemberDetail(
            @ApiIgnore @AuthMember Member member
    ) {
        final MemberDetailResponseDTO res = memberService.readDetail(member);
        return CommonApiResponse.of(res);
    }

    @PatchMapping("/job")
    @ApiOperation(value = "직업 정보를 업데이트 한다 (AccessToken)")
    public CommonApiResponse<MemberCommonResponseDTO> setMemberJob(
            @RequestBody MemberJobUpdateRequestDTO dto,
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(memberService.updateJob(member, dto));
    }

    @PatchMapping("/edu")
    @ApiOperation(value = "학력 정보를 업데이트 한다 (AccessToken)")
    public CommonApiResponse<MemberCommonResponseDTO> setMemberJob(
            @RequestBody MemberEduUpdateRequestDTO dto,
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(memberService.updateEdu(member, dto));
    }


    @PostMapping("/join")
    @ApiOperation(value = "유저를 공통 정보로 가입시킨다 (RegisterToken)")
    public CommonApiResponse<MemberDetailResponseDTO> join(
            HttpServletRequest request,
            @Valid @RequestBody MemberDetailJoinRequestDTO dto
    ) {
        return null;
//        //로그인 상태가 아닌 경우 401
//        if (member == null) {
//            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
//        }
//        final MemberDetailResponseDTO res = memberService.createDetail(member, dto);
//        return CommonApiResponse.of(res);
    }


    @PostMapping("/join/detail")
    @ApiOperation(value = "회원가입 세부 정보를 입력하여 최종 가입시킨다 (AccessToken)")
    public CommonApiResponse<MemberDetailResponseDTO> setMemberDetail(
            @Valid @RequestBody MemberDetailJoinRequestDTO dto,
            @ApiIgnore @AuthMember Member member
    ) {
        final MemberDetailResponseDTO res = memberService.createDetail(member, dto);
        return CommonApiResponse.of(res);
    }

    //페이징 처리 추가할 예정
    @GetMapping("/find")
    @ApiOperation(value = "[Admin]현재 가입한 모든 유저를 불러온다 (AccessToken)")
    public CommonApiResponse<List<MemberCommonResponseDTO>> getMyInformation(
//            @RequestBody RecommendMemberAcceptRequestDTO dto,
//            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(memberService.findAll());
    }

    @PostMapping("/recommend")
    @ApiOperation(value = "다른 유저의 추천사를 작성한다 (AccessToken)")
    public CommonApiResponse<MemberCommonResponseDTO> createRecommend(
            @RequestBody RecommendMemberAcceptRequestDTO dto,
            @ApiIgnore @AuthMember Member member
    ) {
        //로그인 상태가 아닌 경우 401
        memberService.validateLoggedIn(member);
        return CommonApiResponse.of(null);
    }


    /**
     * @// TODO: 2022/10/05 직업 정보가 없는 사람이 가입할 때 직업 정보 입력해야 하는 처리 필요
     * */
    @PatchMapping("/recommend/{uuid}/accept")
    @ApiOperation(value = "요청받은 uuid 추천사에 자신을 추천인으로 등록한다 (AccessToken)")
    public CommonApiResponse<RecommendResponseDTO> updateRecommendByUuid(
            @PathVariable("uuid") String uuid,
            @Valid @RequestBody RecommendMemberAcceptWithUpdateJobRequestDTO dto,
            @ApiIgnore @AuthMember Member member
    ) {
        //로그인 상태가 아닌 경우 401
        memberService.validateFormalMember(member);
        String phone = member.getPhone();
        RecommendResponseDTO recommendResponseDTO = recommendService.updateRecommendMemberAccept(uuid, phone, dto);
        return CommonApiResponse.of(recommendResponseDTO);
    }


    /**
     * 직업 정보가 없을 경우 내 직업 정보를 업데이트하고 추천인을 가입시킨다
     * */
    @PatchMapping("/recommend/{uuid}/accept/job")
    @ApiOperation(value = "요청받은 uuid 추천사에 자신을 추천인으로 등록하며 직업 정보를 업데이트 한다 (AccessToken)")
    public CommonApiResponse<RecommendResponseDTO> updateRecommendByUuidWithJob(
            @PathVariable("uuid") String uuid,
            @Valid @RequestBody RecommendMemberAcceptWithUpdateJobRequestDTO dto,
            @ApiIgnore @AuthMember Member member
    ) {
        //로그인 상태가 아닌 경우 401
        memberService.validateFormalMember(member);
        String phone = member.getPhone();
        RecommendResponseDTO recommendResponseDTO = recommendService.updateRecommendMemberAccept(uuid, phone, dto);
        return CommonApiResponse.of(recommendResponseDTO);
    }

    /**
     * @// TODO: 2022/10/06 인증정보 업데이트 방식 정할것. 직업 또는 학생 둘다 가능하기 떄문
     * */

}
