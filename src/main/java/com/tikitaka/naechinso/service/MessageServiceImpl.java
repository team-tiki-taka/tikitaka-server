package com.tikitaka.naechinso.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikitaka.naechinso.constant.ErrorCode;
import com.tikitaka.naechinso.dto.MessageDTO;
import com.tikitaka.naechinso.dto.RequestMessageDTO;
import com.tikitaka.naechinso.dto.ResponseMessageDTO;
import com.tikitaka.naechinso.exception.BadRequestException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.model.Message;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final WebClient webClient;
    @Value("${NAVER_ACCESS_KEY}")
    private String accessKey;
    @Value("${NAVER_SECRET_KEY}")
    private String secretKey;
    @Value("${NAVER_SMS_ID}")
    private String serviceId;
    @Value("${NAVER_SMS_PHONE_NUMBER}")
    private String senderNumber;

    @Override
    public ResponseMessageDTO sendMessage(MessageDTO messageDto) {
        final String smsURL = "https://sens.apigw.ntruss.com/sms/v2/services/"+ serviceId +"/messages";

        try {
            Long time = System.currentTimeMillis();

            List<MessageDTO> messages = new ArrayList<>();
            messages.add(messageDto);

            RequestMessageDTO requestMessageDTO = RequestMessageDTO.builder()
                    .type("SMS")
                    .contentType("COMM")
                    .countryCode("82")
                    .from(senderNumber)
                    .content(messageDto.getContent())
                    .messages(messages)
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();
            String body = objectMapper.writeValueAsString(requestMessageDTO);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-ncp-apigw-timestamp", time.toString());
            headers.set("x-ncp-iam-access-key", accessKey);
            headers.set("x-ncp-apigw-signature-v2", makeSignature(time));

            WebClient.ResponseSpec responseSpec = webClient.post().uri(smsURL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("x-ncp-apigw-timestamp", time.toString())
                    .header("x-ncp-iam-access-key", accessKey)
                    .header("x-ncp-apigw-signature-v2", makeSignature(time))
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(body))
                    .retrieve();

            ResponseMessageDTO responseMessageDTO
                    = responseSpec.bodyToMono(ResponseMessageDTO.class).block();

            return responseMessageDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException(ErrorCode._BAD_REQUEST, "메세지 발송에 실패하였습니다");
        }
    }

    /**
     * 헤더에 서명 추가
     * */
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

            String message = new StringBuilder()
                    .append(method)
                    .append(space)
                    .append(url)
                    .append(newLine)
                    .append(timestamp)
                    .append(newLine)
                    .append(accessKey)
                    .toString();

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
