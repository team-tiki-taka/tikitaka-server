package com.tikitaka.naechinso.domain.member;

import com.tikitaka.naechinso.domain.member.dto.*;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.member.entity.MemberDetail;
import com.tikitaka.naechinso.global.common.response.TokenResponseDTO;
import com.tikitaka.naechinso.global.config.security.MemberAdapter;
import com.tikitaka.naechinso.global.config.security.dto.JwtDTO;
import com.tikitaka.naechinso.global.config.security.jwt.JwtTokenProvider;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.BadRequestException;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import com.tikitaka.naechinso.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Member findByPhone(String phone) {
        return memberRepository.findByPhone(phone)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public Member findByMember(Member member) {
        return findByPhone(member.getPhone());
    }

    public List<MemberCommonResponseDTO> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberCommonResponseDTO::of).collect(Collectors.toList());
    }


    public MemberCommonJoinResponseDTO joinCommonMember(String phone, MemberCommonJoinRequestDTO dto) {
        //이미 존재하는 유저일 경우 400
        Optional<Member> checkMember = memberRepository.findByPhone(phone);
        if(checkMember.isPresent()) {
            throw new BadRequestException(ErrorCode.USER_ALREADY_EXIST);
        }

        Member member = MemberCommonJoinRequestDTO.toCommonMember(phone, dto);
        memberRepository.save(member);

        TokenResponseDTO tokenResponseDTO
                = jwtTokenProvider.generateToken(new JwtDTO(phone, "ROLE_USER"));

        return MemberCommonJoinResponseDTO.of(member, tokenResponseDTO);
    }

    /**
     * @// TODO: 2022-10-07 이상한점 수정 필요 
     * */
    public MemberCommonJoinResponseDTO updateCommonMember(Member authMember, MemberUpdateCommonRequestDTO dto) {
        //없는 유저면 404
        Member member = findByMember(authMember);
        member.updateCommon(dto);
        memberRepository.save(member);

        TokenResponseDTO tokenResponseDTO
                = jwtTokenProvider.generateToken(new JwtDTO(member.getPhone(), "ROLE_USER"));

        return MemberCommonJoinResponseDTO.of(member);
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


    public MemberDetailResponseDTO readDetail(Member member) {
//
//        Member checkMember = memberRepository.findByPhone(phone)
//                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));

        MemberDetailResponseDTO dto = MemberDetailResponseDTO.of(member);

        return dto;
    }

    public MemberDetailResponseDTO createDetail(Member authMember, MemberDetailJoinRequestDTO dto) {
        //영속성 유지를 위한 fetch
        Member member = findByMember(authMember);

        //detail 정보가 있으면 이미 가입한 회원
        if (member.getDetail() != null) {
            throw new BadRequestException(ErrorCode.USER_ALREADY_EXIST);
        }

        MemberDetail detail = MemberDetail.of(member, dto);
        memberDetailRepository.save(detail);
        return MemberDetailResponseDTO.of(detail);
    }

    public MemberCommonResponseDTO updateJob(Member authMember, MemberJobUpdateRequestDTO dto){
        //영속성 유지를 위한 fetch
        Member member = findByMember(authMember);

        member.updateJob(dto);
        memberRepository.save(member);
        return MemberCommonResponseDTO.of(member);
    }

    public MemberCommonResponseDTO updateEdu(Member authMember, MemberEduUpdateRequestDTO dto){
        //영속성 유지를 위한 fetch
        Member member = findByMember(authMember);

        member.updateEdu(dto);
        memberRepository.save(member);
        return MemberCommonResponseDTO.of(member);
    }


    public void validateToken(Member authMember) {
        if (authMember == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
    }

    public void validateFormalMember(Member authMember) {
        validateToken(authMember);
        if (authMember.getDetail() == null) {
            throw new UnauthorizedException(ErrorCode.FORBIDDEN_USER);
        }
    }

}
