package com.tikitaka.naechinso.domain.recommend;

import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendDTO;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendJoinRequestDTO;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendListResponseDTO;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendRequestDTO;
import com.tikitaka.naechinso.global.annotation.AuthMember;
import com.tikitaka.naechinso.global.config.CommonApiResponse;
import com.tikitaka.naechinso.global.config.security.jwt.JwtTokenProvider;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.UnauthorizedException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;
    private final JwtTokenProvider jwtTokenService;

    @GetMapping("/")
    @ApiOperation(value = "내 추천사 정보를 가져온다 (AccessToken 필요)")
    public CommonApiResponse<RecommendListResponseDTO> getRecommends(
            @ApiIgnore @AuthMember Member member)
    {
        if (member == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        return CommonApiResponse.of(recommendService.readRecommendList(member));
    }

    @PostMapping("/")
    @ApiOperation(value = "추천서를 작성한 후, 추천인을 회원가입 시킨다. (registerToken 필요)")
    public CommonApiResponse<RecommendDTO> createRecommendNewSender(
            HttpServletRequest request,
            @Valid @RequestBody RecommendJoinRequestDTO dto)
    {
        String registerToken = request.getHeader("Authorization");
        String phone = parseRegisterToken(registerToken);

        RecommendDTO recommendDTO = recommendService.createRecommendJoin(phone, dto);
        return CommonApiResponse.of(recommendDTO);
    }

    @PostMapping("/request")
    @ApiOperation(value = "다른 유저에게 추천서 작성을 요청한다 (registerToken 필요)")
    public CommonApiResponse<RecommendDTO> createRecommendRequest(
            HttpServletRequest request,
            @Valid @RequestBody RecommendRequestDTO dto)
    {
        String registerToken = request.getHeader("Authorization");
        String phone = parseRegisterToken(registerToken);
        RecommendDTO recommendDTO = recommendService.createRecommendRequest(phone, dto);
        return CommonApiResponse.of(recommendDTO);
    }








//    //제일 아래에 있어야함
//    @GetMapping("/{uuid}")
//    @ApiOperation(value = "추천 요청 uuid 를 가진 추천사 엔티티를 가져온다")
//    public CommonApiResponse<RecommendDTO> getRecommendByUuid(
//            HttpServletRequest request,
//            @PathVariable("uuid") String uuid,
//            @Valid @RequestBody RecommendRequestDTO dto)
//    {
//        String registerToken = request.getHeader("Authorization");
//        String phone = parseRegisterToken(registerToken);
//
////        RecommendDTO recommendDTO = recommendService.findAllRecommendRequestsByUuid(uuid);
//        return CommonApiResponse.of(recommendDTO);
//    }

    private String parseRegisterToken(String registerToken) {
        if (StringUtils.isBlank(registerToken) || !jwtTokenService.validateToken(registerToken)) {
            throw new UnauthorizedException(ErrorCode.NO_TOKEN);
        }
        return jwtTokenService.parseClaims(registerToken).getSubject();
    }

}
