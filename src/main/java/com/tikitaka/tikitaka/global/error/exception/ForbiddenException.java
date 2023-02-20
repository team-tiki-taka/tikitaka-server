package com.tikitaka.tikitaka.global.error.exception;

import com.tikitaka.tikitaka.global.error.ErrorCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
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
