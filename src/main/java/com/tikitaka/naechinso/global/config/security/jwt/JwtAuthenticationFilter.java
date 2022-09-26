package com.tikitaka.naechinso.global.config.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
        try {
            String jwt = resolveToken(request); //request에서 jwt 토큰을 꺼낸다.

            if (jwt == null) {
                filterChain.doFilter(request, response);
                return;
            }

            System.out.println("jwt = " + jwt); //test

            if (StringUtils.isNotBlank(jwt) && jwtTokenService.validateToken(jwt)) {
                Authentication authentication = jwtTokenService.getAuthentication(jwt); //authentication 획득

//                ((UsernamePasswordAuthenticationToken) authentication).setDetails(
//                        new WebAuthenticationDetailsSource().buildDetails(request)); //기본적으로 제공한 details 세팅

                //Security 세션에서 계속 사용하기 위해 SecurityContext에 Authentication 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                if (StringUtils.isBlank(jwt)) {
                    request.setAttribute("unauthorization", "401 인증키 없음.");
                }

                if (jwtTokenService.validateToken(jwt)) {
                    request.setAttribute("unauthorization", "401-001 인증키 만료.");
                }
            }
        } catch (Exception ex) {
            logger.error("Security Context에 해당 토큰을 등록할 수 없습니다", ex);
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