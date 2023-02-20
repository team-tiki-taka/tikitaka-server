package com.tikitaka.tikitaka.domain.pending;

import com.tikitaka.tikitaka.domain.member.entity.Member;
import com.tikitaka.tikitaka.domain.pending.dto.PendingRejectRequestDTO;
import com.tikitaka.tikitaka.domain.pending.dto.PendingFindResponseDTO;
import com.tikitaka.tikitaka.domain.pending.dto.PendingResponseDTO;
import com.tikitaka.tikitaka.global.annotation.AuthMember;
import com.tikitaka.tikitaka.global.config.CommonApiResponse;
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

    @GetMapping("/{id}")
    @ApiOperation(value = "[Admin]고유 아이디에 해당하는 대기 정보를 가져온다 (AccessToken)")
    public CommonApiResponse<PendingFindResponseDTO> getPendingById(
            @PathVariable("id") Long id,
            @ApiIgnore @AuthMember Member adminMember
    ) {
        return CommonApiResponse.of(PendingFindResponseDTO.of(pendingService.findById(id)));
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
