package com.tikitaka.tikitaka.global.config;

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

    /**
     * Http Response 를 내보낼 때 사용, 데이터를 Json 형식으로 만든다
     * default status code : 200
     * @param data 내보낼 데이터
     * */
    public static <T> CommonApiResponse<T> of(T data) {
        return new CommonApiResponse<>(200, true, data);
    }

    /**
     * Http Response 를 내보낼 때 사용, 데이터를 Json 형식으로 만든다
     * 추가적으로 status code도 지정한다
     * @param data 내보낼 데이터
     * @param status 응답 status code 지정
     * */
    public static <T> CommonApiResponse<T> of(T data, int status) {
        return new CommonApiResponse<>(status, true, data);
    }
}
