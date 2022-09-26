package com.tikitaka.naechinso.global.config.security;

import com.tikitaka.naechinso.domain.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class MemberAccountAdapter extends User {
    private Member member;

    public MemberAccountAdapter(Member member) {
        super(member.getPhone(), "", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.member = member;
    }
}
