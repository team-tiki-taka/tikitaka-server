package com.tikitaka.naechinso.domain.recommend;

import com.tikitaka.naechinso.domain.member.MemberService;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.recommend.dto.*;
import com.tikitaka.naechinso.global.annotation.AuthMember;
import com.tikitaka.naechinso.global.config.CommonApiResponse;
import com.tikitaka.naechinso.global.config.security.jwt.JwtTokenProvider;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.UnauthorizedException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenService;

    @GetMapping
    @ApiOperation(value = "내 추천사 정보를 가져온다 (AccessToken)")
    public CommonApiResponse<RecommendListResponseDTO> getRecommends(
            @ApiIgnore @AuthMember Member member)
    {
        if (member == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        return CommonApiResponse.of(recommendService.readRecommendList(member));
    }

    @PostMapping
    @ApiOperation(value = "다른 유저의 추천사를 작성한다 (AccessToken)")
    public CommonApiResponse<RecommendResponseDTO> createRecommend(
            @Valid @RequestBody RecommendBySenderRequestDTO dto,
            @ApiIgnore @AuthMember Member member
    ) {
        RecommendResponseDTO recommendResponseDTO = recommendService.createRecommend(member, dto);
        return CommonApiResponse.of(recommendResponseDTO);
    }

    @GetMapping("/find")
    @ApiOperation(value = "[Admin]모든 추천사 정보를 가져온다 (AccessToken)")
    public CommonApiResponse<List<RecommendResponseDTO>> getAllRecommends(
            @ApiIgnore @AuthMember Member member)
    {
        if (member == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        return CommonApiResponse.of(recommendService.findAll());
    }

    @PostMapping("/request")
    @ApiOperation(value = "다른 유저에게 추천서 작성을 요청한다 (AccessToken)")
    public CommonApiResponse<RecommendResponseDTO> createRecommendRequest(
            @ApiIgnore @AuthMember Member member)
    {
        RecommendResponseDTO recommendResponseDTO = recommendService.createRecommendRequest(member);
        return CommonApiResponse.of(recommendResponseDTO);
    }




    //제일 아래에 있어야함
    @GetMapping("/{uuid}")
    @ApiOperation(value = "해당 UUID 를 가진 추천사 정보를 가져온다 (Register / AccessToken)")
    public CommonApiResponse<RecommendResponseDTO> getRecommendByUuid(
            HttpServletRequest request,
            @PathVariable("uuid") String uuid)
    {
        //토큰 장착 확인
        String phone = jwtTokenService.parsePhoneByRegisterToken(request);
        RecommendResponseDTO recommendResponseDTO = RecommendResponseDTO.of(recommendService.findByUuid(uuid));
        return CommonApiResponse.of(recommendResponseDTO);
    }

    @PatchMapping("/{uuid}/accept")
    @ApiOperation(value = "해당 UUID 추천사에 자신을 추천인으로 등록한다 (AccessToken)")
    public CommonApiResponse<RecommendResponseDTO> updateRecommendByUuid(
            @ApiIgnore @AuthMember Member member,
            @PathVariable("uuid") String uuid,
            @Valid @RequestBody RecommendAcceptRequestDTO dto)
    {
        RecommendResponseDTO recommendResponseDTO = recommendService.acceptRecommendByUuid(member, uuid, dto);
        return CommonApiResponse.of(recommendResponseDTO);
    }
}
