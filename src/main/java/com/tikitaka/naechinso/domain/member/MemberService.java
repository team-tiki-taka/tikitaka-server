package com.tikitaka.naechinso.domain.member;

import com.tikitaka.naechinso.domain.member.dto.MemberCommonJoinRequestDto;
import com.tikitaka.naechinso.domain.member.dto.MemberCommonResponseDto;
import com.tikitaka.naechinso.domain.member.dto.MemberDetailJoinRequestDto;
import com.tikitaka.naechinso.domain.member.dto.MemberDetailResponseDto;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.member.entity.MemberDetail;
import com.tikitaka.naechinso.global.common.response.TokenResponseDTO;
import com.tikitaka.naechinso.global.config.security.MemberAdapter;
import com.tikitaka.naechinso.global.config.security.dto.JwtDTO;
import com.tikitaka.naechinso.global.config.security.jwt.JwtTokenProvider;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.BadRequestException;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberDetailRepository memberDetailRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public List<MemberCommonResponseDto> findAll() {
        List<MemberCommonResponseDto> memberList = memberRepository.findAll().stream()
                .map(member -> MemberCommonResponseDto.of(member)).collect(Collectors.toList());
        return memberList;
    }


    public MemberCommonResponseDto createCommonMember(MemberCommonJoinRequestDto dto) {

        //이미 존재하는 유저일 경우 400
        Optional<Member> checkMember = memberRepository.findByPhone(dto.getPhone());
        if(!checkMember.isEmpty()) {
            throw new BadRequestException(ErrorCode.USER_ALREADY_EXIST);
        }

        Member member = MemberCommonJoinRequestDto.toCommonMember(dto);
        memberRepository.save(member);

        MemberCommonResponseDto res = MemberCommonResponseDto.of(member);
        return res;
    }

    public TokenResponseDTO login(String phone) {

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


    public MemberDetailResponseDto readDetail(Member member) {
//
//        Member checkMember = memberRepository.findByPhone(phone)
//                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));

        MemberDetailResponseDto dto = MemberDetailResponseDto.of(member);

        return dto;
    }

    public MemberDetailResponseDto createDetail(Member authMember, MemberDetailJoinRequestDto dto) {
        //영속성 유지를 위한 fetch
        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        //detail 정보가 있으면 이미 가입한 회원
        if (member.getDetail() != null) {
            throw new BadRequestException(ErrorCode.USER_ALREADY_EXIST);
        }

        MemberDetail detail = MemberDetail.of(member, dto);
        memberDetailRepository.save(detail);
        return MemberDetailResponseDto.of(detail);
    }
}
