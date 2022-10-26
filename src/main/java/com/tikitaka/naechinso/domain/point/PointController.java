package com.tikitaka.naechinso.domain.point;

import com.tikitaka.naechinso.domain.member.dto.MemberCommonResponseDTO;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.point.dto.PointChargeRequestDTO;
import com.tikitaka.naechinso.domain.point.dto.PointResponseDTO;
import com.tikitaka.naechinso.global.annotation.AuthMember;
import com.tikitaka.naechinso.global.config.CommonApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping("/point")
public class PointController {

    private PointService pointService;

    @GetMapping
    @ApiOperation(value = "유저 자신의 포인트 충전 / 사용 내역을 모두 가져온다 (AccessToken)")
    public CommonApiResponse<PointResponseDTO> getPointHistory(
            @ApiIgnore @AuthMember Member member
    ) {
//        return CommonApiResponse.of(pointService.readCommonMember(member));
        return null;
    }

    @PostMapping("/charge")
    @ApiOperation(value = "유저 자신의 모든 정보를 가져온다 (AccessToken)")
    public CommonApiResponse<PointResponseDTO> getMyInformation(
            @ApiIgnore @AuthMember Member member,
            @RequestBody PointChargeRequestDTO requestDTO
            ) {
        return CommonApiResponse.of(pointService.charge(member, requestDTO));
    }
}
