package com.tikitaka.naechinso.exception;

import com.tikitaka.naechinso.constant.ErrorCode;
import lombok.Getter;

@Getter
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
