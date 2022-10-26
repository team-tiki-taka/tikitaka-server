package com.tikitaka.naechinso.domain.match;

import com.tikitaka.naechinso.domain.card.CardService;
import com.tikitaka.naechinso.domain.match.dto.MatchBasicProfileResponseDTO;
import com.tikitaka.naechinso.domain.match.dto.MatchOpenProfileResponseDTO;
import com.tikitaka.naechinso.domain.match.dto.MatchResponseDTO;
import com.tikitaka.naechinso.domain.match.dto.MatchThumbnailResponseDTO;
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
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;
    private final CardService cardService;
    @GetMapping("/receive")
    @ApiOperation(value = "내가 받은 호감 정보를 가져온다 (AccessToken)")
    public CommonApiResponse<List<MatchThumbnailResponseDTO>> getAllMatchByMemberFrom(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(matchService.findAllByToMember(member));
    }

    @GetMapping("/send")
    @ApiOperation(value = "내가 보낸 호감 정보를 가져온다 (AccessToken)")
    public CommonApiResponse<List<MatchThumbnailResponseDTO>> getAllMatchByMemberTo(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(matchService.findAllByFromMember(member));
    }

    @GetMapping("/complete")
    @ApiOperation(value = "서로 호감을 보낸 성사된 매칭 정보를 가져온다 (AccessToken)")
    public CommonApiResponse<List<MatchThumbnailResponseDTO>> getAllCompleteMatchByMember(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(matchService.findAllByMatchComplete(member));
    }

    @PostMapping("/like")
    @ApiOperation(value = "현재 활성화된 카드의 상대에게 호감을 보낸다 (AccessToken)")
    public CommonApiResponse<MatchResponseDTO> like(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(matchService.like(member));
    }

    @GetMapping("/{memberId}/profile")
    @ApiOperation(value = "고유 아이디에 해당하는 유저 프로필 정보를 가져온다 (AccessToken)")
    public CommonApiResponse<MatchBasicProfileResponseDTO> getBasicProfileById(
            @PathVariable("memberId") Long id,
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(matchService.getBasicProfileById(member, id));
    }

    @GetMapping("/{memberId}/profile/open")
    @ApiOperation(value = "번호 오픈을 사용한 고유 아이디 유저의 프로필 정보를 가져온다 (AccessToken)")
    public CommonApiResponse<MatchOpenProfileResponseDTO> getOpenProfileById(
            @PathVariable("memberId") Long id,
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(matchService.getOpenProfileById(member, id));
    }

    @PostMapping("/{id}/accept")
    @ApiOperation(value = "현재 호감을 보낸 상대를 수락하여 매칭을 성사시킨다 (AccessToken)")
    public CommonApiResponse<MatchResponseDTO> accept(
            @PathVariable("id") Long id,
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(matchService.accept(member, id));
    }

    @PostMapping("/{id}/open")
    @ApiOperation(value = "현재 호감을 보낸 상대의 번호 오픈을 요청한다 (AccessToken)")
    public CommonApiResponse<MatchResponseDTO> openPhone(
            @PathVariable("id") Long id,
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(matchService.openPhone(member, id));
    }
}
