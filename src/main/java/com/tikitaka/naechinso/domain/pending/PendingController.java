package com.tikitaka.naechinso.domain.pending;

import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.pending.dto.PendingRejectRequestDTO;
import com.tikitaka.naechinso.domain.pending.dto.PendingFindResponseDTO;
import com.tikitaka.naechinso.domain.pending.dto.PendingResponseDTO;
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
@RequestMapping("/pending")
@RequiredArgsConstructor
public class PendingController {

    private final PendingService pendingService;

    @GetMapping
    @ApiOperation(value = "내 승인 대기 정보를 가져온다 (AccessToken)")
    public CommonApiResponse<List<PendingResponseDTO>> getPendingByMember(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(pendingService.findAllByMemberId(member.getId()));
    }

    @GetMapping("/find")
    @ApiOperation(value = "[Admin]모든 가입 대기 정보를 가져온다 (AccessToken)")
    public CommonApiResponse<List<PendingFindResponseDTO>> getAllPending(
            @ApiIgnore @AuthMember Member adminMember
    ) {
        return CommonApiResponse.of(pendingService.findAll());
    }

    @PostMapping("/{id}/accept")
    @ApiOperation(value = "[Admin] 요청을 승인한다 (AccessToken)")
    public CommonApiResponse<PendingFindResponseDTO> acceptPending(
            @PathVariable("id") Long id,
            @ApiIgnore @AuthMember Member adminMember
    ) {
        return CommonApiResponse.of(pendingService.acceptPending(adminMember, id));
    }

    @PostMapping("/{id}/reject")
    @ApiOperation(value = "요청을 거부한다 (AccessToken)")
    public CommonApiResponse<PendingFindResponseDTO> rejectPending(
            @PathVariable("id") Long id,
            @RequestBody PendingRejectRequestDTO dto,
            @ApiIgnore @AuthMember Member adminMember
    ) {
        return CommonApiResponse.of(pendingService.rejectPending(adminMember, id, dto));
    }
}
