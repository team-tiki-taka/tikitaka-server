package com.tikitaka.tikitaka.infra.notification.dto;

import com.tikitaka.tikitaka.infra.notification.constant.NotificationType;
import com.tikitaka.tikitaka.infra.notification.entity.Notification;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class NotificationHistoryResponseDTO {
    //알림 이력
    private List<History> history;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    @ToString
    private static class History {
        private NotificationType type;
        private String title;
        private String content;
        private String date;
    }

    public static NotificationHistoryResponseDTO of(List<Notification> notifications) {
        List<History> historyList = new ArrayList<>();
        for (Notification notification : notifications) {
            historyList.add(History.builder()
                    .type(notification.getType())
                    .title(notification.getTitle())
                    .content(notification.getContent())
                    .date(notification.getCreatedAt().toString())
                    .build());
        }

        return NotificationHistoryResponseDTO.builder()
                .history(historyList)
                .build();
    }
}
