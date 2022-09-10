package com.tikitaka.naechinso.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** API 응답 공통 형식입니다
 * @author gengminy (220812) */
@AllArgsConstructor
@Getter
public class CommonApiResponse<T> {

    private final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    private int status;
    private boolean success;
    private T data;

    public static <T> CommonApiResponse<T> of(T data) {
        return new CommonApiResponse<>(200, true, data);
    }

    public static <T> CommonApiResponse<T> of(T data, int status) {
        return new CommonApiResponse<>(status, true, data);
    }
}
