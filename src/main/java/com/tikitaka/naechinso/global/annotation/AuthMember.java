package com.tikitaka.naechinso.global.annotation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * request 의 Authorization 헤더를 가지고
 * JwtAuthenticationFilter 를 통과하면서
 * SecurityContextHolder 에 로그인 된 유저의
 * UserDetails 의 커스텀 구현체 MemberAdapter 를 가져온 후
 * member property 를 추출하는 어노테이션
 * @author gengimny 220926
 * */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : member")
public @interface AuthMember {
}
