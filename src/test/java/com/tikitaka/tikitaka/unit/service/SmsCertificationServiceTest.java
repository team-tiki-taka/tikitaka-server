package com.tikitaka.tikitaka.unit.service;

import com.tikitaka.tikitaka.domain.member.MemberRepository;
import com.tikitaka.tikitaka.domain.member.entity.Member;
import com.tikitaka.tikitaka.domain.recommend.RecommendService;
import com.tikitaka.tikitaka.domain.sms.SmsCertificationServiceImpl;
import com.tikitaka.tikitaka.domain.sms.dto.SmsCertificationRequestDTO;
import com.tikitaka.tikitaka.domain.sms.dto.SmsCertificationSuccessResponseDTO;
import com.tikitaka.tikitaka.global.common.response.TokenResponseDTO;
import com.tikitaka.tikitaka.global.config.redis.RedisService;
import com.tikitaka.tikitaka.global.config.security.dto.JwtDTO;
import com.tikitaka.tikitaka.global.config.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
public class SmsCertificationServiceTest {

    @InjectMocks
    private SmsCertificationServiceImpl smsCertificationService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private RecommendService recommendService;
    @Mock
    private RedisService redisService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    //Redis Config
    private final String VERIFICATION_PREFIX = "sms:";
    private final int VERIFICATION_TIME_LIMIT = 3 * 60;

    @DisplayName("가짜 메세지 전송 성공과 레디스 저장 테스트")
    @Test
    void testSendFakeSms() {
        //given
        final String receiver = "01012345678";
        final Duration verificationTimeLimit = Duration.ofSeconds(VERIFICATION_TIME_LIMIT);

        //when
        String verificationCode = smsCertificationService.sendVerificationMessage(receiver)
                .substring(12, 18);

        //then
        verify(redisService, times(1))
                .setValues(VERIFICATION_PREFIX + receiver, verificationCode, verificationTimeLimit);
        assertThat(verificationCode.length()).isEqualTo(6);
    }


    @DisplayName("가입하지 않은 회원의 RegisterToken 발급 테스트")
    @Test
    void testVerifyCodeByNotSignedUpMember() {
        //given
        final String receiver = "01012345678";
        final Duration verificationTimeLimit = Duration.ofSeconds(VERIFICATION_TIME_LIMIT);
        final String verificationCode = smsCertificationService.sendVerificationMessage(receiver)
                .substring(12, 18);
        final SmsCertificationRequestDTO requestDTO
                = SmsCertificationRequestDTO.builder()
                .phoneNumber(receiver)
                .code(verificationCode)
                .build();
        final String key = VERIFICATION_PREFIX + receiver;

        given(redisService.hasKey(key)).willReturn(true);
        given(redisService.getValues(key)).willReturn(verificationCode);
        given(memberRepository.findByPhone(receiver)).willReturn(Optional.empty());
        given(recommendService.findAllRecommendReceivedListBasicByPhone(receiver)).willReturn(new ArrayList<>());
        given(jwtTokenProvider.generateRegisterToken(new JwtDTO(receiver))).willReturn("rgT");

        //when
        SmsCertificationSuccessResponseDTO responseDTO 
                = smsCertificationService.verifyCode(requestDTO);

        //then
        verify(redisService, times(1))
                .setValues(VERIFICATION_PREFIX + receiver, verificationCode, verificationTimeLimit);
        verify(redisService, times(1)).hasKey(key);
        verify(redisService, times(1)).getValues(key);
        verify(memberRepository, times(1)).findByPhone(receiver);
        verify(recommendService, times(1)).findAllRecommendReceivedListBasicByPhone(receiver);
        verify(jwtTokenProvider, times(1)).generateRegisterToken(new JwtDTO(receiver));
        assertThat(responseDTO.getAccessToken()).isNull();
        assertThat(responseDTO.getRefreshToken()).isNull();
        assertThat(responseDTO.getRegisterToken()).isEqualTo("rgT");
//        assertThat(responseDTO.getRecommendReceived()).isFalse();
    }

    @DisplayName("가입한 회원의 Token 발급 테스트")
    @Test
    void testVerifyCodeBySignedUpMember() {
        //given
        final String receiver = "01012345678";
        final Duration verificationTimeLimit = Duration.ofSeconds(VERIFICATION_TIME_LIMIT);
        final String verificationCode = smsCertificationService.sendVerificationMessage(receiver)
                .substring(12, 18);
        final SmsCertificationRequestDTO requestDTO = SmsCertificationRequestDTO.builder()
                .phoneNumber(receiver)
                .code(verificationCode)
                .build();
        final String key = VERIFICATION_PREFIX + receiver;
        final Member member = Member.builder().build();

        given(redisService.hasKey(key)).willReturn(true);
        given(redisService.getValues(key)).willReturn(verificationCode);
        given(memberRepository.findByPhone(receiver)).willReturn(Optional.of(member));
        given(recommendService.findAllRecommendReceivedListBasicByPhone(receiver)).willReturn(new ArrayList<>());
        given(jwtTokenProvider.generateToken(new JwtDTO(receiver, "ROLE_USER")))
                .willReturn(TokenResponseDTO.builder().accessToken("aT").refreshToken("rfT").build());

        //when
        SmsCertificationSuccessResponseDTO responseDTO
                = smsCertificationService.verifyCode(requestDTO);

        //then
        verify(redisService, times(1))
                .setValues(VERIFICATION_PREFIX + receiver, verificationCode, verificationTimeLimit);
        verify(redisService, times(1)).hasKey(key);
        verify(redisService, times(1)).getValues(key);
        verify(memberRepository, times(1)).findByPhone(receiver);
        verify(recommendService, times(1)).findAllRecommendReceivedListBasicByPhone(receiver);
        verify(jwtTokenProvider, times(1)).generateToken(new JwtDTO(receiver, "ROLE_USER"));

        assertThat(responseDTO.getAccessToken()).isEqualTo("aT");
        assertThat(responseDTO.getRefreshToken()).isEqualTo("rfT");
        assertThat(responseDTO.getRegisterToken()).isNull();
//        assertThat(responseDTO.getRecommendReceived()).isFalse();
    }

//
//    @DisplayName("인증번호 시간 만료 테스트")
//    @Test
//    void testVerifyCodeBySignedUpMember() {
//        //given
//        final String receiver = "01012345678";
//        final Duration verificationTimeLimit = Duration.ofSeconds(VERIFICATION_TIME_LIMIT);
//        final String verificationCode = smsCertificationService.sendVerificationMessage(receiver)
//                .substring(12, 18);
//        final SmsCertificationRequestDTO requestDTO = SmsCertificationRequestDTO.builder()
//                .phoneNumber(receiver)
//                .code(verificationCode)
//                .build();
//        final String key = VERIFICATION_PREFIX + receiver;
//
//        given(redisService.hasKey(key)).willReturn(true);
//        given(redisService.getValues(key)).willReturn(verificationCode);
//        given(memberRepository.findByPhone(receiver)).willReturn(Optional.empty());
//        given(recommendService.existsByReceiverPhoneAndSenderNotNull(receiver)).willReturn(false);
//        given(jwtTokenProvider.generateRegisterToken(new JwtDTO(receiver))).willReturn("rT");
//
//        //when
//        SmsCertificationSuccessResponseDTO responseDTO
//                = smsCertificationService.verifyCode(requestDTO);
//
//        //then
//        verify(redisService, times(1))
//                .setValues(VERIFICATION_PREFIX + receiver, verificationCode, verificationTimeLimit);
//        verify(redisService, times(1)).hasKey(key);
//        verify(redisService, times(1)).getValues(key);
//        verify(memberRepository, times(1)).findByPhone(receiver);
//        verify(recommendService, times(1)).existsByReceiverPhoneAndSenderNotNull(receiver);
//        verify(jwtTokenProvider, times(1)).generateRegisterToken(new JwtDTO(receiver));
//        assertThat(responseDTO.getAccessToken()).isNull();
//        assertThat(responseDTO.getRefreshToken()).isNull();
//        assertThat(responseDTO.getRegisterToken()).isEqualTo("rT");
//        assertThat(responseDTO.getRecommendReceived()).isFalse();
//    }

}
