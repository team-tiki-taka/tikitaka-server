package com.tikitaka.naechinso.domain.match;

import com.tikitaka.naechinso.domain.card.CardService;
import com.tikitaka.naechinso.domain.match.dto.MatchListResponseDTO;
import com.tikitaka.naechinso.domain.match.dto.MatchResponseDTO;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.annotation.AuthMember;
import com.tikitaka.naechinso.global.config.CommonApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;
    private final CardService cardService;
    @GetMapping
    @ApiOperation(value = "매칭페이지에 진입 시 자신의 모든 매칭 정보를 가져온다 (AccessToken)")
    public CommonApiResponse<MatchListResponseDTO> getAllMatchByMember(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(matchService.findAllByMember(member));
    }

    @PostMapping("/like/send")
    @ApiOperation(value = "현재 활성화된 카드의 상대에게 호감을 보낸다 (AccessToken)")
    public CommonApiResponse<MatchListResponseDTO> sendLike(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(matchService.findAllByMember(member));
    }

    @PostMapping("/like/{id}/accept")
    @ApiOperation(value = "현재 호감을 보낸 상대를 수락하여 매칭을 성사시킨다 (AccessToken)")
    public CommonApiResponse<MatchListResponseDTO> acceptLike(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(matchService.findAllByMember(member));
    }
}
