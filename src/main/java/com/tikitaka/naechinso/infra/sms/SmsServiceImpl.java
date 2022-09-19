package com.tikitaka.naechinso.infra.sms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.BadRequestException;
import com.tikitaka.naechinso.infra.sms.dto.NaverSmsMessageDTO;
import com.tikitaka.naechinso.infra.sms.dto.NaverSmsRequestDTO;
import com.tikitaka.naechinso.infra.sms.dto.NaverSmsResponseDTO;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {
    private final WebClient webClient;
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
     * @param message 메세지
     * @return 성공시 true
     */
    @Override
    public boolean sendMessage(String to, String message) {
        final String smsURL = "https://sens.apigw.ntruss.com/sms/v2/services/"+ serviceId +"/messages";
        final Long time = System.currentTimeMillis();

        try {
            //네이버 sms 메세지 dto
            final NaverSmsMessageDTO naverSmsMessageDTO = new NaverSmsMessageDTO(to, message);
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

            webClient.post().uri(smsURL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("x-ncp-apigw-timestamp", time.toString())
                    .header("x-ncp-iam-access-key", accessKey)
                    .header("x-ncp-apigw-signature-v2", makeSignature(time))
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(body))
                    .retrieve()
                    .bodyToMono(NaverSmsResponseDTO.class).block();

            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * sms 전송을 위한 서명을 추가한다
     * @param currentTime 현재 시간
     * @return 서명
     */
    private String makeSignature(Long currentTime) {
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
}
