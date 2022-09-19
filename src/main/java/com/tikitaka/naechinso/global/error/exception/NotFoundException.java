package com.tikitaka.naechinso.global.error.exception;

import com.tikitaka.naechinso.global.error.ErrorCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NotFoundException extends BusinessException {
    private String message;

    public NotFoundException(String message) {
        super(ErrorCode._BAD_REQUEST);
        this.message = message;
    }

    public NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode);
        this.message = message;
    }

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
