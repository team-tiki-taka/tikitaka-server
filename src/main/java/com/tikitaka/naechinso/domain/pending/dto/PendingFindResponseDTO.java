package com.tikitaka.naechinso.domain.pending.dto;

import com.tikitaka.naechinso.domain.pending.constant.PendingType;
import com.tikitaka.naechinso.domain.pending.entity.Pending;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PendingFindResponseDTO {

    private Long id;

    private Long memberId;

    private Long adminId;

    private PendingType type;

    private Boolean isAccepted;

    private String reason;

    private List<String> images;


    private LocalDateTime createdAt;


    public static PendingFindResponseDTO of(Pending pending) {
        Long memberId;
        if (pending.getMember() == null) {
            memberId = null;
        } else {
            memberId = pending.getMember().getId();
        }

        return PendingFindResponseDTO.builder()
                .id(pending.getId())
                .memberId(memberId)
                .type(pending.getType())
                .isAccepted(pending.getIsAccepted())
                .reason(pending.getReason())
                .images(pending.getImages())
                .adminId(pending.getAdminId())
                .createdAt(pending.getCreatedAt())
                .build();
    }
}
