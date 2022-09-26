package com.tikitaka.naechinso.domain.member;

import com.tikitaka.naechinso.domain.member.dto.MemberCommonJoinRequestDto;
import com.tikitaka.naechinso.domain.member.dto.MemberCommonJoinResponseDto;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.annotation.AuthMember;
import com.tikitaka.naechinso.global.config.CommonApiResponse;
import com.tikitaka.naechinso.global.config.security.LoginServiceImpl;
import com.tikitaka.naechinso.global.config.security.MemberAccountAdapter;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.UnauthorizedException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public CommonApiResponse<Member> getMyInformation(HttpServletRequest request, @ApiIgnore @AuthenticationPrincipal MemberAccountAdapter accountAdapter) {

        System.out.println("SecurityContextHolder.getContext() = " + SecurityContextHolder.getContext());
        System.out.println("SecurityContextHolder.getContext().getAuthentication() = " + SecurityContextHolder.getContext().getAuthentication());


        Member member = accountAdapter.getMember();

        if (member == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }

//        return CommonApiResponse.of(MemberCommonJoinResponseDto.of(userDetails));
        return CommonApiResponse.of(member);

    }



    @PostMapping("/join")
    public CommonApiResponse<MemberCommonJoinResponseDto> joinCommonMember(@Valid @RequestBody MemberCommonJoinRequestDto dto) {
        final MemberCommonJoinResponseDto res = memberService.joinCommonMember(dto);
        return CommonApiResponse.of(res);
    }

//    @PostMapping("/login")


//    @GetMapping("/find")
//    public CommonApiResponse<String> findAllMember() {
//        final String a = memberService.getMember();
//        return CommonApiResponse.of(a);
//    }

}
