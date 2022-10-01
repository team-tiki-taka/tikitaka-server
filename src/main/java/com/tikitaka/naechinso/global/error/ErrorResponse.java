package com.tikitaka.naechinso.global.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/** 예외처리를 위한 클래스입니다
 * @author gengminy (220812) */
@Getter
@Builder
@Slf4j
public class ErrorResponse {
    private final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    private final boolean success = false;
    private int status;
    private String message;
    private List<ErrorField> errors;
    private String code;

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .message(errorCode.getDetail())
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.getCode())
                .errors(Arrays.asList())
                .build();
    }

    //dto 검증을 위한 생성자
    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
        return ErrorResponse.builder()
                .message(errorCode.getDetail())
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.getCode())
                .errors(ErrorField.of(bindingResult))
                .build();
    }

    //filter chain 을 위한 JSON 생성자
    public static JSONObject jsonOf(ErrorCode errorCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp", LocalDateTime.now());
        jsonObject.put("success", false);
        jsonObject.put("message", errorCode.getDetail());
        jsonObject.put("status", errorCode.getHttpStatus().value());
        jsonObject.put("code", errorCode.getCode());
        return jsonObject;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorField {
        private String field;

        private String value;

        private String reason;

        public static List<ErrorField> of(BindingResult bindingResult) {
            try {
                List<ErrorField> errorFields =
                        bindingResult.getAllErrors().stream().map(error ->
                        {
                            FieldError fieldError = (FieldError) error;

                            return new ErrorField(
                                    fieldError.getField(),
                                    Objects.toString(fieldError.getRejectedValue()),
                                    fieldError.getDefaultMessage());
                        }).collect(Collectors.toList());
                return errorFields;
            } catch (Exception e) {
                System.out.println("e = " + e);
                return Arrays.asList();
            }
        }
    }
}