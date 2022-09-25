package com.tikitaka.naechinso.global.config.security;

import com.tikitaka.naechinso.domain.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

@Getter
public class UserAccount extends User {
    private Member member;

    public UserAccount(Member member) {
        super(member.getPhone(), "", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.member = member;
    }
}
