package com.tikitaka.naechinso.domain.member;

import com.tikitaka.naechinso.domain.member.dto.MemberCommonJoinRequestDto;
import com.tikitaka.naechinso.domain.member.dto.MemberCommonJoinResponseDto;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.common.response.TokenResponseDTO;
import com.tikitaka.naechinso.global.config.security.MemberAdapter;
import com.tikitaka.naechinso.global.config.security.dto.JwtDTO;
import com.tikitaka.naechinso.global.config.security.jwt.JwtTokenProvider;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberDetailRepository memberDetailRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberCommonJoinResponseDto joinCommonMember(MemberCommonJoinRequestDto dto) {

        //이미 존재하는 유저일 경우 400
        Optional<Member> checkMember = memberRepository.findByPhone(dto.getPhone());
        if(!checkMember.isEmpty()) {
            throw new BadRequestException(ErrorCode.USER_ALREADY_EXIST);
        }

        Member member = MemberCommonJoinRequestDto.toCommonMember(dto);
        memberRepository.save(member);

        MemberCommonJoinResponseDto res = MemberCommonJoinResponseDto.of(member);
        return res;
    }

    public TokenResponseDTO login(String phone) {

        System.out.println("phone = " + phone);
        Member checkMember = memberRepository.findByPhone(phone)
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(new MemberAdapter(checkMember), "", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        TokenResponseDTO tokenResponseDTO
                = jwtTokenProvider.generateToken(new JwtDTO(phone));

        //리프레시 토큰 저장 로직 아래에
        return tokenResponseDTO;
    }

//
//    public MemberCommonJoinResponseDto joinMemberWithDetail(MemberCommonJoinRequestDto dto) {
//        //이미 존재하는 유저일 경우 400
//        Optional<Member> checkMember = memberRepository.findByPhone(dto.getPhone());
//        if(!checkMember.isEmpty()) {
//            throw new BadRequestException(ErrorCode.USER_ALREADY_EXIST);
//        }
//
//        Member member = MemberCommonJoinRequestDto.toCommonMember(dto);
//        memberRepository.save(member);
//
//        MemberCommonJoinResponseDto res = MemberCommonJoinResponseDto.of(member);
//        return res;
//    }

}
