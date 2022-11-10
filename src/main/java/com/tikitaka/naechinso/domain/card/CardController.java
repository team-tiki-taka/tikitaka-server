package com.tikitaka.naechinso.domain.card;

import com.tikitaka.naechinso.domain.card.dto.CardCountResponseDTO;
import com.tikitaka.naechinso.domain.card.dto.CardOppositeMemberProfileResponseDTO;
import com.tikitaka.naechinso.domain.card.dto.CardResponseDTO;
import com.tikitaka.naechinso.domain.card.dto.CardThumbnailResponseDTO;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.annotation.AuthMember;
import com.tikitaka.naechinso.global.config.CommonApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @GetMapping
    @ApiOperation(value = "자신이 소유한 모든 카드 썸네일을 가져온다 (AccessToken)")
    public CommonApiResponse<List<CardThumbnailResponseDTO>> getAllCardsByMember(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(cardService.findAllCard(member));
    }

    @GetMapping("/find")
    @ApiOperation(value = "[Admin]존재하는 모든 카드를 가져온다 (AccessToken)")
    public CommonApiResponse<List<CardResponseDTO>> getAllCards(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(cardService.findAllDTO());
    }

    @GetMapping("/count")
    @ApiOperation(value = "오늘의 남은 추천 횟수를 반환한다 (AccessToken)")
    public CommonApiResponse<CardCountResponseDTO> getRemainingCount(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(cardService.getRemainingCount(member));
    }

    @GetMapping("/profile")
    @ApiOperation(value = "현재 활성화된 카드의 상대 프로필과 추천인 정보를 가져온다 (AccessToken)")
    public CommonApiResponse<CardOppositeMemberProfileResponseDTO> getProfileByCard(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(cardService.getCardProfileById(member));
    }

    @PostMapping
    @ApiOperation(value = "새로운 추천을 받는다 (AccessToken)")
    public CommonApiResponse<CardThumbnailResponseDTO> createCardByMember(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(cardService.createRandomCard(member));
    }

    @PatchMapping("/reject")
    @ApiOperation(value = "현재 활성화된 카드를 거절한다 (AccessToken)")
    public CommonApiResponse<CardResponseDTO> rejectCardByMember(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(cardService.rejectCard(member));
    }
}
