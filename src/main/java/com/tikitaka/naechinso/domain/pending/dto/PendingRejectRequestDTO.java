package com.tikitaka.naechinso.domain.pending.dto;

import com.tikitaka.naechinso.domain.pending.constant.PendingType;
import com.tikitaka.naechinso.domain.pending.entity.Pending;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PendingRejectRequestDTO {

    private PendingType type;

    private Boolean isAccepted;

    private String reason;

    private List<String> images;

    private Long adminId;

}
