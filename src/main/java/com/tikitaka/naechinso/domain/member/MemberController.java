package com.tikitaka.naechinso.domain.member;

import com.tikitaka.naechinso.global.config.CommonApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/save")
    public CommonApiResponse<Long> saveMember() {
        final Long a = memberService.saveMember();
        return CommonApiResponse.of(a);
    }

    @GetMapping("/save-detail")
    public CommonApiResponse<Long> saveMemberWithDetail() {
        final Long a = memberService.saveMemberWithDetail();
        return CommonApiResponse.of(a);
    }

    @GetMapping("/find")
    public CommonApiResponse<String> findAllMember() {
        final String a = memberService.getMember();
        return CommonApiResponse.of(a);
    }

}
