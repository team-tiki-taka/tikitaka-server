package com.tikitaka.naechinso.global.config.security;


import com.tikitaka.naechinso.domain.member.MemberRepository;
import com.tikitaka.naechinso.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        Member member = memberRepository.findByPhone(phone)
                .orElseThrow(() -> { throw new UsernameNotFoundException(phone + "-> DB에 없는 유저"); });

        return new MemberAdapter(member);
//        return new User(member.getPhone(), "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
//
//    //DB에 존재하는 유저일 경우 UserDetails로 만들어서 반환
//    private UserDetails createUserDetails(Member member) {
//        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
//    }
}