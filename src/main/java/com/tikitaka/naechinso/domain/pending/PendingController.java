package com.tikitaka.naechinso.domain.pending;

import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.pending.dto.PendingRejectRequestDTO;
import com.tikitaka.naechinso.domain.pending.dto.PendingFindResponseDTO;
import com.tikitaka.naechinso.global.config.CommonApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
//@RequestMapping("/admin/pending")
@RequestMapping("/pending")
@RequiredArgsConstructor
public class PendingController {

    private final PendingService pendingService;

    @GetMapping
    @ApiOperation(value = "[Admin]모든 가입 대기 정보를 가져온다 (AccessToken)")
    public CommonApiResponse<List<PendingFindResponseDTO>> getAllPending(
//            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(pendingService.findAll());
    }

    @PostMapping
    @ApiOperation(value = "[테스트]가입 대기 정보 생성 (AccessToken)")
    public CommonApiResponse<List<PendingFindResponseDTO>> createPending(
            @RequestBody PendingRejectRequestDTO dto
//            @ApiIgnore @AuthMember Member member
    ) {

        return CommonApiResponse.of(pendingService.findAll());
    }

    @PostMapping("/accept/{id}")
    @ApiOperation(value = "[테스트]요청을 승인한다 (AccessToken)")
    public CommonApiResponse<PendingFindResponseDTO> acceptPending(
            @PathVariable("id") Long id
//            @ApiIgnore @AuthMember Member member
    ) {
        Member member = Member.builder().id(10L).build(); /////

        return CommonApiResponse.of(pendingService.acceptPending(member, id));
    }
}
