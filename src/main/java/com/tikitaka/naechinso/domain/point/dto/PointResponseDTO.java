package com.tikitaka.naechinso.domain.point.dto;

import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.point.constant.PointType;
import com.tikitaka.naechinso.domain.point.entity.Point;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class PointResponseDTO {
    private long value;
    private String content;
    private PointType type;
    private String date;

    // 유저 잔여 포인트
    private long point;

    public static PointResponseDTO of(Member member, Point point) {
        return PointResponseDTO.builder()
                .value(point.getValue())
                .content(point.getContent())
                .type(point.getType())
                .date(point.getCreatedAt().toString())
                .point(member.getPoint())
                .build();
    }
}
