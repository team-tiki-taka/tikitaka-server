package com.tikitaka.naechinso.domain.pending.dto;

import com.tikitaka.naechinso.domain.pending.constant.PendingType;
import com.tikitaka.naechinso.domain.pending.entity.Pending;
import lombok.*;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 사진 변경 및 인증 승인 요청에 대한 유저용 응답 DTO
 * @author gengminy 221010
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PendingResponseDTO {

    private PendingType type;

    private Boolean isAccepted;

    private String reason;

    private Map<String, Object> content;

    private List<String> images;

    private List<String> rejectImages;

    private String createdAt;

    private String updatedAt;


    public static PendingResponseDTO of(Pending pending) {
        Map<String, Object> content;
        if (pending.getContent() != null) {
            content = new JSONObject(pending.getContent()).toMap();
        } else {
            content = null;
        }

        return PendingResponseDTO.builder()
                .type(pending.getType())
                .isAccepted(pending.getIsAccepted())
                .reason(pending.getReason())
                .rejectImages(pending.getRejectImages())
                .content(content)
                .images(pending.getImages())
                .createdAt(pending.getCreatedAt().toString())
                .updatedAt(pending.getUpdatedAt().toString())
                .build();
    }
}
