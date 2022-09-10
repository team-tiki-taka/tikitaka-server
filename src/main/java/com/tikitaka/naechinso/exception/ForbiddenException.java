package com.tikitaka.naechinso.exception;

import com.tikitaka.naechinso.constant.ErrorCode;
import lombok.Getter;

@Getter
public class ForbiddenException extends BusinessException {
    private String message;

    public ForbiddenException(String message) {
        super(ErrorCode._BAD_REQUEST);
        this.message = message;
    }

    public ForbiddenException(ErrorCode errorCode, String message) {
        super(errorCode);
        this.message = message;
    }

    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
