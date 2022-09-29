package com.tikitaka.naechinso.domain.member;

import com.tikitaka.naechinso.domain.member.dto.MemberCommonJoinRequestDto;
import com.tikitaka.naechinso.domain.member.dto.MemberCommonResponseDto;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.annotation.AuthMember;
import com.tikitaka.naechinso.global.config.CommonApiResponse;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.UnauthorizedException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    @ApiOperation(value = "유저 자신의 정보를 가져온다 (AccessToken 필요)")
    public CommonApiResponse<MemberCommonResponseDto> getMyInformation(
            HttpServletRequest request, @ApiIgnore @AuthMember Member member) {
        //로그인 상태가 아닌 경우 401
        if (member == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        return CommonApiResponse.of(MemberCommonResponseDto.of(member));
    }

    @PostMapping("/join")
    @ApiOperation(value = "공통 유저를 회원가입 시킨다")
    public CommonApiResponse<MemberCommonResponseDto> joinCommonMember(
            @Valid @RequestBody MemberCommonJoinRequestDto dto)
    {
        final MemberCommonResponseDto res = memberService.joinCommonMember(dto);
        return CommonApiResponse.of(res);
    }

    @PostMapping("/detail")
    @ApiOperation(value = "회원가입 세부 정보를 입력한다 (AccessToken 필요)")
    public CommonApiResponse<MemberCommonResponseDto> setMemberDetail(
            @Valid @RequestBody MemberCommonJoinRequestDto dto,
            @ApiIgnore @AuthMember Member member)
    {
        //로그인 상태가 아닌 경우 401
        if (member == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        final MemberCommonResponseDto res = memberService.joinCommonMember(dto);
        return CommonApiResponse.of(res);
    }

//    @PostMapping("/login")


//    @GetMapping("/find")
//    public CommonApiResponse<String> findAllMember() {
//        final String a = memberService.getMember();
//        return CommonApiResponse.of(a);
//    }

}
