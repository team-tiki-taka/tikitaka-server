package com.tikitaka.naechinso.global.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateUtil {
    public static long getDueDay(LocalDateTime createdAt, long addDay) {
        LocalDateTime endDatetime = LocalDateTime.of(createdAt.toLocalDate().plusDays(addDay), LocalTime.of(23,59,59));
        long dueDay = Duration.between(LocalDateTime.now(), endDatetime).toDays();
        if (dueDay < 0) {
            return 0;
        }
        return dueDay;
    }
}
