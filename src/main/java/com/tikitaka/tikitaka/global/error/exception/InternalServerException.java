package com.tikitaka.tikitaka.global.error.exception;

import com.tikitaka.tikitaka.global.error.ErrorCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class InternalServerException extends BusinessException {
    private String message;

    public InternalServerException(String message) {
        super(ErrorCode._BAD_REQUEST);
        this.message = message;
    }

    public InternalServerException(ErrorCode errorCode, String message) {
        super(errorCode);
        this.message = message;
    }

    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}
