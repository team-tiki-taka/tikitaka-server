package com.tikitaka.naechinso.domain.member;

import com.tikitaka.naechinso.domain.member.dto.MemberCommonJoinRequestDto;
import com.tikitaka.naechinso.domain.member.dto.MemberCommonResponseDto;
import com.tikitaka.naechinso.domain.member.dto.MemberDetailJoinRequestDto;
import com.tikitaka.naechinso.domain.member.dto.MemberDetailResponseDto;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.annotation.AuthMember;
import com.tikitaka.naechinso.global.config.CommonApiResponse;
import com.tikitaka.naechinso.global.config.security.jwt.JwtTokenProvider;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    @GetMapping("/")
    @ApiOperation(value = "유저 자신의 모든 정보를 가져온다 (AccessToken 필요)")
    public CommonApiResponse<MemberCommonResponseDto> getMyInformation(
            HttpServletRequest request, @ApiIgnore @AuthMember Member member) {
        //로그인 상태가 아닌 경우 401
        if (member == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        return CommonApiResponse.of(MemberCommonResponseDto.of(member));
    }

//    @PostMapping("/join")
//    @ApiOperation(value = "공통 유저를 기본 정보로 회원가입 시킨다 (registerToken 필요)")
//    public CommonApiResponse<MemberCommonResponseDto> joinCommonMember(
//            HttpServletRequest request,
//            @Valid @RequestBody MemberCommonJoinRequestDto dto)
//    {
//        String registerToken = request.getHeader("Authorization");
//        if (StringUtils.isBlank(registerToken) || !jwtTokenService.validateToken(registerToken)) {
//            throw new UnauthorizedException(ErrorCode.NO_TOKEN);
//        }
//
//        final MemberCommonResponseDto res = memberService.createCommonMember(dto);
//        return CommonApiResponse.of(res);
//    }

    @GetMapping("/detail")
    @ApiOperation(value = "회원가입 세부 정보를 가져온다 (AccessToken 필요)")
    public CommonApiResponse<MemberDetailResponseDto> getMemberDetail(
            @ApiIgnore @AuthMember Member member)
    {
        //로그인 상태가 아닌 경우 401
        if (member == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        final MemberDetailResponseDto res = memberService.readDetail(member);
        return CommonApiResponse.of(res);
    }

    @PostMapping("/join")
    @ApiOperation(value = "회원가입 세부 정보를 입력하여 최종 가입시킨다 (AccessToken 필요)")
    public CommonApiResponse<MemberDetailResponseDto> setMemberDetail(
            @Valid @RequestBody MemberDetailJoinRequestDto dto,
            @ApiIgnore @AuthMember Member member)
    {
        //로그인 상태가 아닌 경우 401
        if (member == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        final MemberDetailResponseDto res = memberService.createDetail(member, dto);
        return CommonApiResponse.of(res);
    }

    //페이징 처리 추가할 예정
    @GetMapping("/find")
    @ApiOperation(value = "현재 가입한 모든 유저를 불러온다")
    public CommonApiResponse<List<MemberCommonResponseDto>> getMyInformation() {
        return CommonApiResponse.of(memberService.findAll());
    }

    @PostMapping("/recommend")
    @ApiOperation(value = "다른 유저의 추천사를 작성한다")
    public CommonApiResponse<MemberCommonResponseDto> createRecommend() {
        return CommonApiResponse.of(null);
    }

//    @PostMapping("/login")


//    @GetMapping("/find")
//    public CommonApiResponse<String> findAllMember() {
//        final String a = memberService.getMember();
//        return CommonApiResponse.of(a);
//    }

}
