package com.tikitaka.naechinso.domain.card;

import com.tikitaka.naechinso.domain.match.MatchService;
import com.tikitaka.naechinso.domain.match.dto.MatchListResponseDTO;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.annotation.AuthMember;
import com.tikitaka.naechinso.global.config.CommonApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @GetMapping
    @ApiOperation(value = "자신이 소유한 모든 카드를 가져온다 (AccessToken)")
    public CommonApiResponse<Object> getAllCardsByMember(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(cardService.findAllByMember(member));
    }

    @GetMapping("/find")
    @ApiOperation(value = "[Admin]존재하는 모든 카드를 가져온다 (AccessToken)")
    public CommonApiResponse<Object> getAllCards(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(cardService.findAll());
    }


    @PostMapping
    @ApiOperation(value = "새로운 카드를 하나 생성한다 (AccessToken)")
    public CommonApiResponse<Object> createCardByMember(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(cardService.createCard(member));
    }
}
