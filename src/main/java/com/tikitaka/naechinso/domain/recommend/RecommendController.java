package com.tikitaka.naechinso.domain.recommend;

import com.tikitaka.naechinso.domain.member.dto.MemberCommonResponseDto;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendListResponseDTO;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendRequestDTO;
import com.tikitaka.naechinso.global.annotation.AuthMember;
import com.tikitaka.naechinso.global.config.CommonApiResponse;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.UnauthorizedException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/")
    @ApiOperation(value = "내 추천사 정보를 가져온다 (AccessToken 필요)")
    public CommonApiResponse<RecommendListResponseDTO> getRecommends(
            HttpServletRequest request,
            @ApiIgnore @AuthMember Member member)
    {
        if (member == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        return CommonApiResponse.of(recommendService.getRecommendList(member));
    }

    @PostMapping("/")
    @ApiOperation(value = "추천사를 작성한다 (AccessToken 필요)")
    public CommonApiResponse<MemberCommonResponseDto> writeRecommend(
            HttpServletRequest request,
            @RequestBody RecommendRequestDTO dto,
            @ApiIgnore @AuthMember Member member)
    {
        if (member == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        return CommonApiResponse.of(MemberCommonResponseDto.of(member));
    }
}
