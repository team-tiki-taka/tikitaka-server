package com.tikitaka.naechinso.domain.point.dto;

import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.point.constant.PointType;
import com.tikitaka.naechinso.domain.point.entity.Point;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class PointHistoryResponseDTO {
    //포인트 이력
    private List<History> history;

    //남은 포인트
    private long point = 0;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    @ToString
    private static class History {
        private long value;
        private String content;
        private PointType type;
        private String date;
        //잔액 기록
        private long point;
    }

    public static PointHistoryResponseDTO of(List<Point> points) {
        List<History> historyList = new ArrayList<>();
        long pointSum = 0;
        for (Point pointHistory : points) {
            if (pointHistory.getType() == PointType.CHARGE) {
                pointSum += pointHistory.getValue();
            } else if (pointHistory.getType() == PointType.USE) {
                pointSum -= pointHistory.getValue();
            }

            historyList.add(History.builder()
                    .content(pointHistory.getContent())
                    .date(pointHistory.getCreatedAt().toString())
                    .type(pointHistory.getType())
                    .value(pointHistory.getValue())
                    .point(pointSum)
                    .build());
        }

        return PointHistoryResponseDTO.builder()
                .history(historyList)
                .point(pointSum)
                .build();
    }
}
