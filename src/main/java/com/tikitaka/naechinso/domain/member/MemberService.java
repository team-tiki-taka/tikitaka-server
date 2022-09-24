package com.tikitaka.naechinso.domain.member;

import com.tikitaka.naechinso.domain.member.dto.MemberCommonJoinRequestDto;
import com.tikitaka.naechinso.domain.member.dto.MemberCommonJoinResponseDto;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.member.entity.MemberDetail;
import com.tikitaka.naechinso.global.common.response.TokenResponseDTO;
import com.tikitaka.naechinso.global.config.security.dto.JwtDTO;
import com.tikitaka.naechinso.global.config.security.jwt.JwtTokenProvider;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(phone, null);

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때
        //    LoginServiceImpl의 loadUserByUsername 메서드가 실행됨
        Authentication authentication
                = authenticationManagerBuilder.getObject().authenticate(authenticationToken);



        System.out.println("1 = " + 1);

        TokenResponseDTO tokenResponseDTO
                = jwtTokenProvider.generateToken(new JwtDTO(phone));

        System.out.println("2 = " + 2);

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
