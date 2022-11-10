package com.tikitaka.naechinso.global.config.security;

import com.tikitaka.naechinso.domain.member.constant.Role;
import com.tikitaka.naechinso.domain.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;
import java.util.stream.Collectors;

/**
 * UserDetails 를 구현한 어댑터
 * Spring Security Authentication 사용 시
 * Member Entity 를 가져오기 편하게 하기 위해 구현
 * @author gengminy 220926
 * */
@Getter
public class MemberAdapter extends User {
    private Member member;

    public MemberAdapter(Member member) {
        super(member.getPhone(), "", authorities(member.getRole()));
        this.member = member;
    }

    private static Collection<? extends GrantedAuthority> authorities(Role userRole) {
        Collection<GrantedAuthority> role = new ArrayList<>();
        role.add(new SimpleGrantedAuthority(userRole.getDetail()));
        return role;
    }
}
