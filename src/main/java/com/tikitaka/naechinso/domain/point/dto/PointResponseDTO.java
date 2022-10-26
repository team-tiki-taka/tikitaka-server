package com.tikitaka.naechinso.domain.point.dto;

import com.tikitaka.naechinso.domain.point.constant.PointType;
import com.tikitaka.naechinso.domain.point.entity.Point;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class PointResponseDTO {
    private int value;
    private String content;
    private PointType type;
    private String date;

    public static PointResponseDTO of(Point point) {
        return PointResponseDTO.builder()
                .value(point.getValue())
                .content(point.getContent())
                .type(point.getType())
                .date(point.getCreatedAt().toString())
                .build();
    }
}
