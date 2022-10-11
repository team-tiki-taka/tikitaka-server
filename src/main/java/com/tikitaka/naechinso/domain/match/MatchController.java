package com.tikitaka.naechinso.domain.match;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;
//    @GetMapping
//    @ApiOperation(value = "자신의 매칭 정보를 가져온다 (AccessToken)")
//    public CommonApiResponse<String> getAllMatchByMember(
//            @ApiIgnore @AuthMember Member member
//    ) {
//        return CommonApiResponse.of(member.toString());
//    }
}
