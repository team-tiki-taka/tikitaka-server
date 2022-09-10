package com.tikitaka.naechinso.config;

import com.tikitaka.naechinso.constant.ErrorCode;
import com.tikitaka.naechinso.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

/** 전역 예외 처리를 하기 위한 핸들러 입니다 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
    /**
     *  비즈니스 로직 익셉션 처리하는 핸들러
     */
    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("handleMethodArgumentNotValidException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    /**
     *  javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     *  HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     *  주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.DUPLICATE_RESOURCE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


//    /**
//     * enum type 일치하지 않아 binding 못할 경우 발생
//     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
//     */
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
//        log.error("handleMethodArgumentTypeMismatchException", e);
//        final ErrorResponse response = ErrorResponse.of(();
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode._METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode._UNAUTHORIZED);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 로그인 정보가 일치하지 않을 때
     */
    @ExceptionHandler({BadCredentialsException.class})
    protected ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        log.error("handleBadCredentialsException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.LOGIN_FAILED);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleAllException(Exception e) {
        log.error("Exception : {}", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode._INTERNAL_SERVER_ERROR);
        log.info(Arrays.stream(e.getStackTrace()).toString());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}