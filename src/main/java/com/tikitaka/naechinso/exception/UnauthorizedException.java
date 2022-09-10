package com.tikitaka.naechinso.exception;

import com.tikitaka.naechinso.constant.ErrorCode;
import lombok.Getter;

@Getter
public class UnauthorizedException extends BusinessException {
    private String message;

    public UnauthorizedException(String message) {
        super(ErrorCode._BAD_REQUEST);
        this.message = message;
    }

    public UnauthorizedException(ErrorCode errorCode, String message) {
        super(errorCode);
        this.message = message;
    }

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
