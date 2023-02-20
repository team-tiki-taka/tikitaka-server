package com.tikitaka.tikitaka.global.config.security.jwt;

import com.tikitaka.tikitaka.global.error.ErrorCode;
import com.tikitaka.tikitaka.global.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * ControllerAdvice 에서는 Spring Security 예외를 잡아낼 수 없기 때문에
 * AuthenticationEntryPoint 구현체를 통해 예외처리를 커스텀한다
 * @author gengminy 220926
 * */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException e
    ) throws IOException {

        String exception = (String)request.getAttribute("exception");

        //토큰이 없을 경우
        if(exception == null || exception.equals(ErrorCode.NO_TOKEN.getCode())) {
            setResponse(response, ErrorCode.NO_TOKEN);
        }
        //잘못된 타입의 토큰인 경우
        else if(exception.equals(ErrorCode.INVALID_AUTH_TOKEN.getCode())) {
            setResponse(response, ErrorCode.INVALID_AUTH_TOKEN);
        }
        //DB에 정보가 없는 토큰을 사용했울 때
        else if(exception.equals(ErrorCode.INVALID_USER_TOKEN.getCode())) {
            setResponse(response, ErrorCode.INVALID_USER_TOKEN);
        }
        //잘못된 서명
        else if(exception.equals(ErrorCode.INVALID_SIGNATURE.getCode())) {
            setResponse(response, ErrorCode.INVALID_SIGNATURE);
        }
        //토큰 만료된 경우
        else if(exception.equals(ErrorCode.EXPIRED_TOKEN.getCode())) {
            setResponse(response, ErrorCode.EXPIRED_TOKEN);
        }
        //지원되지 않는 토큰인 경우
        else if(exception.equals(ErrorCode.UNSUPPORTED_TOKEN.getCode())) {
            setResponse(response, ErrorCode.UNSUPPORTED_TOKEN);
        }
        else {
            setResponse(response, ErrorCode.FORBIDDEN_USER);
        }
    }

    /**
     * 스프링 시큐티리 예외 커스텀을 위한 함수
     */
    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().print(ErrorResponse.jsonOf(errorCode));
    }
}
