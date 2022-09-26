package com.tikitaka.naechinso.global.config.security.jwt;

import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.ErrorResponse;
import com.tikitaka.naechinso.global.error.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

        if(exception == null || exception.equals(ErrorCode.NO_TOKEN.getCode())) {
            setResponse(response, ErrorCode.NO_TOKEN);
        }
        //잘못된 타입의 토큰인 경우
        else if(exception.equals(ErrorCode.INVALID_AUTH_TOKEN.getCode())) {
            setResponse(response, ErrorCode.INVALID_AUTH_TOKEN);
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
