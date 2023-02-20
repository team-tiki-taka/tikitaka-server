package com.tikitaka.tikitaka.global.config.security.jwt;

import com.tikitaka.tikitaka.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //Spring Security 에 저장되어 있지 않으면 헤더에서 jwt 토큰을 가지고옴
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                String jwt = resolveToken(request); //request에서 jwt 토큰을 꺼낸다.

                if (jwt == null) {
                    log.error("jwt 값을 가져올 수 없습니다");
                    request.setAttribute("exception", ErrorCode.NO_TOKEN.getCode());
                    filterChain.doFilter(request, response);
                    return;
                }

//                // 만료 10분 전 리프레시 토큰으로 reissue
//                if (canRefresh(claims, 6000 * 10)) {
//                    String refreshedToken = jwt.refreshToken(authorizationToken);
//                    response.setHeader(headerkey, refreshedToken);
//                }

                if (StringUtils.isNotBlank(jwt) && jwtTokenService.validateToken(jwt)) {
                    Authentication authentication = jwtTokenService.getAuthentication(request, jwt); //authentication 획득

                    //Security 세션에서 계속 사용하기 위해 SecurityContext에 Authentication 등록
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    if (StringUtils.isBlank(jwt)) {
                        request.setAttribute("exception", ErrorCode.NO_TOKEN.getCode());
                    }

                    jwtTokenService.validateToken(request, jwt);
                }
            } catch (Exception ex) {
                logger.error("Security Context에 해당 토큰을 등록할 수 없습니다", ex);
            }
        }
        else {
            log.debug("SecurityContextHolder not populated with security token, as it already contained: '{}'",
                    SecurityContextHolder.getContext().getAuthentication());
        }

        filterChain.doFilter(request, response);
    }
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        //Prefix 로 Bearer 가 있으면 제거
        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        //Prefix 가 없으면 그대로
        return bearerToken;

    }
}