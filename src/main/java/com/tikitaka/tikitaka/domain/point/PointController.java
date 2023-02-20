package com.tikitaka.tikitaka.domain.point;

import com.tikitaka.tikitaka.domain.member.entity.Member;
import com.tikitaka.tikitaka.domain.point.dto.PointChargeRequestDTO;
import com.tikitaka.tikitaka.domain.point.dto.PointHistoryResponseDTO;
import com.tikitaka.tikitaka.domain.point.dto.PointResponseDTO;
import com.tikitaka.tikitaka.global.annotation.AuthMember;
import com.tikitaka.tikitaka.global.config.CommonApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @GetMapping
    @ApiOperation(value = "유저 자신의 포인트 충전 / 사용 내역을 모두 가져온다 (AccessToken)")
    public CommonApiResponse<PointHistoryResponseDTO> getPointHistory(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(pointService.getPointHistory(member));
    }

    @PostMapping("/charge")
    @ApiOperation(value = "포인트 충전을 요청한다 (AccessToken)")
    public CommonApiResponse<PointResponseDTO> chargePoint(
            @ApiIgnore @AuthMember Member member,
            @RequestBody PointChargeRequestDTO requestDTO
            ) {
        return CommonApiResponse.of(pointService.charge(member, requestDTO));
    }

    @PostMapping("/use")
    @ApiOperation(value = "포인트 사용을 요청한다 (AccessToken)")
    public CommonApiResponse<PointResponseDTO> usePoint(
            @ApiIgnore @AuthMember Member member,
            @RequestBody PointChargeRequestDTO requestDTO
    ) {
        return CommonApiResponse.of(pointService.use(member, requestDTO));
    }
}
