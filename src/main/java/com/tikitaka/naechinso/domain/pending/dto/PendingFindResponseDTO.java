package com.tikitaka.naechinso.domain.pending.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tikitaka.naechinso.domain.pending.constant.PendingType;
import com.tikitaka.naechinso.domain.pending.entity.Pending;
import lombok.*;
import org.json.JSONObject;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 사진 변경 및 인증 승인 요청에 대한 어드민용 응답 DTO
 * @author gengminy 221010
 * */
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

    private Map<String, Object> content;

    private Boolean isAccepted;

    private String reason;

    private List<String> images;

    private List<String> rejectImages;

    private String createdAt;

    public static PendingFindResponseDTO of(Pending pending) {
        Long memberId;
        Map<String, Object> content;
        if (pending.getMember() == null) {
            memberId = null;
        } else {
            memberId = pending.getMember().getId();
        }

        if (pending.getContent() != null) {
            content = new JSONObject(pending.getContent()).toMap();
        } else {
            content = null;
        }

        return PendingFindResponseDTO.builder()
                .id(pending.getId())
                .memberId(memberId)
                .type(pending.getType())
                .content(content)
                .reason(pending.getReason())
                .isAccepted(pending.getIsAccepted())
                .images(pending.getImages())
                .reason(pending.getReason())
                .rejectImages(pending.getRejectImages())
                .adminId(pending.getAdminId())
                .createdAt(pending.getCreatedAt().toString())
                .build();
    }
}
