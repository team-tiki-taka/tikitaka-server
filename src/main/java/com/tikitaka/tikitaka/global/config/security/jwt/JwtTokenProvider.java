package com.tikitaka.tikitaka.global.config.security.jwt;

import com.tikitaka.tikitaka.global.common.response.TokenResponseDTO;
import com.tikitaka.tikitaka.global.config.security.UserDetailServiceImpl;
import com.tikitaka.tikitaka.global.config.security.dto.JwtDTO;
import com.tikitaka.tikitaka.global.error.ErrorCode;
import com.tikitaka.tikitaka.global.error.exception.BadRequestException;
import com.tikitaka.tikitaka.global.error.exception.UnauthorizedException;
import com.tikitaka.tikitaka.global.config.redis.RedisService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final RedisService redisService;
    private final UserDetailServiceImpl loginService;
    /** 토큰 비밀 키 */
    @Value("${jwt.secret-key}")
    private String JWT_SECRET;

    /** 토큰 유효 시간 (ms) */
    private static final long REGISTER_TOKEN_EXPIRATION_MS =  1000L * 60 * 60; //60분
    private static final long JWT_EXPIRATION_MS = 1000L * 60 * 40; //40분
    private static final long REFRESH_TOKEN_EXPIRATION_MS = 1000L * 60 * 60 * 24 * 7; //7일
    private static final String AUTHORITIES_KEY = "role"; //권한 정보 컬럼명

    public String generateAccessToken(JwtDTO jwtDTO) {
        //권한 가져오기

        final String encodedKey = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes());
        final Date now = new Date();
        final Date accessTokenExpiresIn = new Date(now.getTime() + JWT_EXPIRATION_MS);


        final String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("naechinso")
                .setIssuedAt(now) // 생성일자 지정(현재)
                .setSubject(jwtDTO.getPhoneNumber()) // 사용자(principal => phoneNumber)
                .claim(AUTHORITIES_KEY, jwtDTO.getRole()) //권한 설정
                .setExpiration(accessTokenExpiresIn) // 만료일자
                .signWith(SignatureAlgorithm.HS512, encodedKey) // signature에 들어갈 secret 값 세팅
                .compact();

        return accessToken;
    }

    public String generateRefreshToken(JwtDTO jwtDTO) {
        final String encodedKey = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes());
        final Date now = new Date();
        final Date refreshTokenExpiresIn = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_MS);

        final String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("naechinso")
                .setExpiration(refreshTokenExpiresIn)
                .signWith(SignatureAlgorithm.HS512, encodedKey)
                .compact();

        //redis에 해당 phone number 의 리프레시 토큰 등록
        redisService.setValues(
                jwtDTO.getPhoneNumber(),
                refreshToken,
                Duration.ofMillis(REFRESH_TOKEN_EXPIRATION_MS)
        );

        return refreshToken;
    }

    /** Jwt 토큰 생성
     * @param jwtDto 인증 요청하는 유저 정보
     */
    public TokenResponseDTO generateToken(JwtDTO jwtDto)
            throws HttpServerErrorException.InternalServerError {
        //권한 가져오기
        final String accessToken = generateAccessToken(jwtDto);
        final String refreshToken = generateRefreshToken(jwtDto);

        return TokenResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /** 회원가입 전용 Register Token 생성
     * @param jwtDTO 인증 요청하는 유저 정보
     */
    public String generateRegisterToken(JwtDTO jwtDTO)
            throws HttpServerErrorException.InternalServerError {
        final String encodedKey = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes());
        final Date now = new Date();
        final Date registerTokenExpiresIn = new Date(now.getTime() + REGISTER_TOKEN_EXPIRATION_MS);

        //권한 정보를 제외하고 생성
        final String registerToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("naechinso")
                .setIssuedAt(now) // 생성일자 지정(현재)
                .setSubject(jwtDTO.getPhoneNumber()) // 사용자(principal => phoneNumber)
                .setExpiration(registerTokenExpiresIn) // 만료일자
                .signWith(SignatureAlgorithm.HS512, encodedKey) // signature 에 들어갈 secret 값 세팅
                .compact();

        return registerToken;
    }

    /** Redis 에서 RegistrerToken 을 제거
     * @param phone 로그아웃 요청 유저
     * @return true if redis 서버에 토큰이 있었을 경우
     * false if 토큰이 없었을 경우
     */
    public boolean deleteRegisterToken(String phone) {
        try {
            if (redisService.hasKey(phone)) {
                redisService.deleteValues(phone);
                return true;
            }
        } catch (Exception e) {
            log.error("Redis 로그아웃 요청을 실패했습니다");
        }
        return false;
    }

    public Authentication getAuthentication(HttpServletRequest request, String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            request.setAttribute("exception", ErrorCode.INVALID_AUTH_TOKEN.getCode());
            throw new UnauthorizedException(ErrorCode.INVALID_AUTH_TOKEN);
        }

        try {
            UserDetails principal = loginService.loadUserByUsername(claims.getSubject());
            return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
        } catch (Exception e) {
            request.setAttribute("exception", ErrorCode.INVALID_USER_TOKEN.getCode());
            throw e;
        }
    }


    /**
     * JWT 유효성 검사
     * @param token 검사하려는 JWT 토큰
     * @returns boolean
     * @throws SignatureException 서명이 다를때
     * @throws MalformedJwtException JWT 구조가 아닐때
     * @throws ExpiredJwtException 만료기간이 지났을때
     * @throws UnsupportedJwtException 지원 불가
     * @throws IllegalArgumentException 매개변수 전달 오류
     */
    public boolean validateToken(String token) {
        final String encodedKey = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes());
        try {
            Jwts.parser().setSigningKey(encodedKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException ex) {
            log.error("잘못된 JWT 서명입니다");
        } catch (ExpiredJwtException ex) {
            log.error("만료된 JWT 토큰입니다");
        } catch (UnsupportedJwtException ex) {
            log.error("지원하지 않는 JWT 토큰입니다");
        } catch (IllegalArgumentException ex) {
            log.error("JWT 토큰이 비어있습니다");
        }
        return false;
    }

    /**
     * JWT 유효성 검사 오버로딩, 에러 커스텀을 위한 함수
     * @param token 검사하려는 JWT 토큰
     * @returns boolean
     * */
    public boolean validateToken(HttpServletRequest request, String token) {
        final String encodedKey = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes());
        try {
            Jwts.parser().setSigningKey(encodedKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException ex) {
            log.error("잘못된 JWT 서명입니다");
            request.setAttribute("exception", ErrorCode.INVALID_SIGNATURE.getCode());
        } catch (ExpiredJwtException ex) {
            log.error("만료된 JWT 토큰입니다");
            request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN.getCode());
        } catch (UnsupportedJwtException ex) {
            log.error("지원하지 않는 JWT 토큰입니다");
            request.setAttribute("exception", ErrorCode.UNSUPPORTED_TOKEN.getCode());
        } catch (IllegalArgumentException ex) {
            log.error("JWT 토큰이 비어있습니다");
            request.setAttribute("exception", ErrorCode.NO_TOKEN.getCode());
        }
        return false;
    }
    /**
     * 토큰 예외 중 만료 상황만 검증 함수
     * @param token 검사하려는 JWT 토큰
     * @returns boolean
     * */
    public boolean validateTokenExceptExpiration(String token) {
        final String encodedKey = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes());
        try {
            Jwts.parser().setSigningKey(encodedKey).parseClaimsJws(token);
            return true;
//            Jws<Claims> claims = Jwts.parser().setSigningKey(encodedKey).parseClaimsJws(token);
//            return claims.getBody().getExpiration().before(new Date());
        } catch(ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Redis Memory 의 RefreshToken 과
     * User 의 RefreshToken 이 일치하는지 확인
     * @param phone 검증하려는 유저 휴대전화
     * @param refreshToken 검증하려는 리프레시 토큰
     */
    public void validateRefreshToken(String phone, String refreshToken) {
        String redisRefreshToken = redisService.getValues(phone);
        if (!refreshToken.equals(redisRefreshToken)) {
            throw new BadRequestException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }
    }

    /**
     * Request Header 에서 RegisterToken 파싱 및 phone 추출
     * @param request Auth Token 장착한 Request
     * @return phone 휴대폰
     * */
    public String parsePhoneByRegisterToken(HttpServletRequest request) {
        String registerToken = request.getHeader("Authorization");
        if (StringUtils.isBlank(registerToken) || !validateToken(registerToken)) {
            throw new UnauthorizedException(ErrorCode.NO_TOKEN);
        }
        return parseClaims(registerToken).getSubject();
    }

    /**
     * JWT 토큰에서 claims 추출
     * @param accessToken 추출하고 싶은 AccessToken (JWT)
     * @return Claims
     */
    public Claims parseClaims(String accessToken) {
        final String encodedKey = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes());

        try {
            return Jwts.parser()
                    .setSigningKey(encodedKey)
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
