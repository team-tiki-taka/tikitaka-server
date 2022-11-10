package com.tikitaka.naechinso.global.config.security.jwt;

import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.ErrorResponse;
import com.tikitaka.naechinso.global.error.exception.ForbiddenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** 유저 정보는 있지만 권한이 없을 때를 처리하는 핸들러 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        // 필요한 권한이 없이 접근하려 할때 403
        ErrorCode errorCode = ErrorCode.FORBIDDEN_USER;
        setResponse(response, errorCode);
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