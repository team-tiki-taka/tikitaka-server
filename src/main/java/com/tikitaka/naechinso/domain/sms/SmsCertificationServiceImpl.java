package com.tikitaka.naechinso.domain.sms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikitaka.naechinso.global.error.ErrorCode;
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
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsCertificationServiceImpl implements SmsCertificationService {

    private final WebClient webClient;
    private final RedisService redisService;

    private final String VERIFICATION_PREFIX = "sms:";
    private final int VERIFICATION_TIME_LIMIT = 3 * 60;

    @Value("${SPRING_PROFILE}")
    private String springProfile;
    @Value("${NAVER_ACCESS_KEY}")
    private String accessKey;
    @Value("${NAVER_SECRET_KEY}")
    private String secretKey;
    @Value("${NAVER_SMS_ID}")
    private String serviceId;
    @Value("${NAVER_SMS_PHONE_NUMBER}")
    private String senderNumber;

    /**
     * 인증번호가 담긴 메세지를 전송한다
     * @param to 수신자
     * @return 네이버 api 서버 메세지 응답
     */
    @Override
    public String sendVerificationMessage(String to) {
        final String smsURL = "https://sens.apigw.ntruss.com/sms/v2/services/"+ serviceId +"/messages";
        final Long time = System.currentTimeMillis();
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
            final NaverSmsMessageDTO naverSmsMessageDTO = new NaverSmsMessageDTO(to, generateMessageWithCode(verificationCode));
            List<NaverSmsMessageDTO> messages = new ArrayList<>();
            messages.add(naverSmsMessageDTO);
            final NaverSmsRequestDTO naverSmsRequestDTO = NaverSmsRequestDTO.builder()
                    .type("SMS")
                    .contentType("COMM")
                    .countryCode("82")
                    .from(senderNumber)
                    .content(naverSmsMessageDTO.getContent())
                    .messages(messages)
                    .build();
            final String body = new ObjectMapper().writeValueAsString(naverSmsRequestDTO);

//            final ResponseMessageDTO responseMessageDTO =
            webClient.post().uri(smsURL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("x-ncp-apigw-timestamp", time.toString())
                    .header("x-ncp-iam-access-key", accessKey)
                    .header("x-ncp-apigw-signature-v2", makeSignature(time))
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(body))
                    .retrieve()
                    .bodyToMono(NaverSmsResponseDTO.class).block();

            //redis 에 3분 제한의 인증번호 토큰 저장
            redisService.setValues(VERIFICATION_PREFIX + to, verificationCode, verificationTimeLimit);

            return "메세지 전송 성공";
//            return responseMessageDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException(ErrorCode._BAD_REQUEST, "메세지 발송에 실패하였습니다");
        }
    }

    @Override
    public String verifyCode(SmsCertificationRequestDTO smsCertificationRequestDto) {
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

        //필터를 모두 통과, 인증이 완료되었으니 redis 에서 전화번호 삭제
        redisService.deleteValues(key);
        return "인증에 성공하였습니다";
    }


    /**
     * sms 전송을 위한 서명을 추가한다
     * @param currentTime 현재 시간
     * @return 서명
     */
    @Override
    public String makeSignature(Long currentTime) {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/" + this.serviceId + "/messages";
        String timestamp = currentTime.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        try {

            String message = method +
                    space +
                    url +
                    newLine +
                    timestamp +
                    newLine +
                    accessKey;

            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            String encodeBase64String = Base64.encodeBase64String(rawHmac);

            return encodeBase64String;
        } catch (Exception e) {
            throw new BadRequestException(ErrorCode._BAD_REQUEST, e.getMessage());
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
