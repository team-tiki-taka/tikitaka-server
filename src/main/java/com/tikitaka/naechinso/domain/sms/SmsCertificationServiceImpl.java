package com.tikitaka.naechinso.domain.sms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikitaka.naechinso.domain.member.MemberRepository;
import com.tikitaka.naechinso.domain.member.MemberService;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.common.response.TokenResponseDTO;
import com.tikitaka.naechinso.global.config.security.dto.JwtDTO;
import com.tikitaka.naechinso.global.config.security.jwt.JwtTokenProvider;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.InternalServerException;
import com.tikitaka.naechinso.infra.sms.SmsService;
import com.tikitaka.naechinso.infra.sms.dto.NaverSmsMessageDTO;
import com.tikitaka.naechinso.infra.sms.dto.NaverSmsRequestDTO;
import com.tikitaka.naechinso.domain.sms.dto.SmsCertificationRequestDTO;
import com.tikitaka.naechinso.infra.sms.dto.NaverSmsResponseDTO;
import com.tikitaka.naechinso.global.error.exception.BadRequestException;
import com.tikitaka.naechinso.global.error.exception.UnauthorizedException;
import com.tikitaka.naechinso.global.config.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsCertificationServiceImpl implements SmsCertificationService {

    private final SmsService smsService;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final String VERIFICATION_PREFIX = "sms:";
    private final int VERIFICATION_TIME_LIMIT = 3 * 60;

    @Value("${spring.profiles.active}")
    private String springProfile;

    /**
     * 인증번호가 담긴 메세지를 전송한다
     * @param to 수신자
     * @return 전송 성공시 메세지
     */
    @Override
    public String sendVerificationMessage(String to) {
        //랜덤 6자리 인증번호
        final String verificationCode = generateVerificationCode();
        //3분 제한시간
        final Duration verificationTimeLimit = Duration.ofSeconds(VERIFICATION_TIME_LIMIT);

        //[local, dev] 배포 환경이 아닐때는 fake service 를 제공합니다
        if (!springProfile.equals("prod")) {
            log.info("스프링 프로파일(" + springProfile + ") 따라 fake 서비스를 제공합니다");
            String message = generateMessageWithCode(verificationCode);
            log.info(message);
            redisService.setValues(VERIFICATION_PREFIX + to, verificationCode, verificationTimeLimit);
            return message;
        }

        //[prod] 실 배포 환경에서는 문자를 전송합니다
        try {
            //네이버 sms 메세지 dto
            if (smsService.sendMessage(to, generateMessageWithCode(verificationCode))) {
                //전송 성공하면 redis 에 3분 제한의 인증번호 토큰 저장
                redisService.setValues(VERIFICATION_PREFIX + to, verificationCode, verificationTimeLimit);
                return "메세지 전송 성공";
            } else {
                throw new BadRequestException(ErrorCode._BAD_REQUEST, "메세지 전송에 실패하였습니다");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException(ErrorCode._BAD_REQUEST, "메세지 발송에 실패하였습니다");
        }
    }

    /**
     * 인증번호를 검증한다
     * @param smsCertificationRequestDto {phoneNumber: 휴대폰 번호, code: 인증번호}
     * @return TokenResponseDTO { accessToken, refreshToken }
     */
    @Override
    public TokenResponseDTO verifyCode(SmsCertificationRequestDTO smsCertificationRequestDto) {
        String phoneNumber = smsCertificationRequestDto.getPhoneNumber();
        String code = smsCertificationRequestDto.getCode();
        String key = VERIFICATION_PREFIX + phoneNumber;

        //redis 에 해당 번호의 키가 없는 경우는 인증번호(3분) 만료로 처리
        if (!redisService.hasKey(key)) {
            throw new UnauthorizedException(ErrorCode.EXPIRED_VERIFICATION_CODE);
        }

        //redis 에 해당 번호의 키와 인증번호가 일치하지 않는 경우
        if (!redisService.getValues(key).equals(code)) {
            throw new UnauthorizedException(ErrorCode.MISMATCH_VERIFICATION_CODE);
        }

        //redis 인증 필터 성공하면
        try {
            /* VERIFY FLOW
            * -> 아직 MemberDetail 작성한 회원이 아닐 경우
            * 1. 이미 추천사를 받은 신규회원
            * 2. 추천사를 받아야 하는 신규회원
            * 3. 추천사 써주려는 신규회원
            * 4. 추천 요청을 받아서 추천사를 써주는 신규회원
            * * 입력이 다 되었는지 검증, 안되면 register 토큰 발급
            *
            * -> MemberDetail 검증 완료된 회원
            * 5. 즉시 마이페이지 이동
            * */


            //가입 안된 회원일 경우 null 리턴
            Optional<Member> checkMember = memberRepository.findByPhone(phoneNumber);
            if (checkMember.isEmpty()) {
                return null;
            }

            if (checkMember.get().getDetail() != null) {
                System.out.println("checkMember.get().getDetail() = " + checkMember.get().getDetail());
            } else {
                System.out.println("detail is null");
            }

            //이미 가입한 회원이면
            //인증한 휴대폰 번호로 로그인 후 토큰 생성
            TokenResponseDTO tokenResponseDTO = memberService.login(phoneNumber);

            //redis 에서 번호 제거
            redisService.deleteValues(key);

            //토큰 반환
            return tokenResponseDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerException("jwt 토큰 생성 에러");
        }
    }

    /**
     * 랜덤 인증번호를 생성한다
     * @return 인증번호 6자리
     */
    private String generateVerificationCode() {
        Random random = new Random();
        int verificationCode = random.nextInt(888888) + 111111;
        return Integer.toString(verificationCode);
    }

    /**
     * 인증번호가 포함된 메세지를 생성한다
     * @param code 인증번호 6자리
     * @return 인증번호 6자리가 포함된 메세지
     */
    private String generateMessageWithCode(String code) {
        final String provider = "내친소";
        return "[" + provider + "] 인증번호 [" + code + "] 를 입력해주세요 :)";
    }
}
