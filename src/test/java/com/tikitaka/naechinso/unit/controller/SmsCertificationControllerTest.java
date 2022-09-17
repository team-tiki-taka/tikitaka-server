//package com.tikitaka.naechinso.unit.controller;
//
//import com.tikitaka.naechinso.domain.sms.SmsCertificationController;
//import com.tikitaka.naechinso.domain.sms.dto.SmsVerificationCodeRequestDTO;
//import com.tikitaka.naechinso.global.config.redis.RedisService;
//import com.tikitaka.naechinso.domain.sms.SmsCertificationService;
//import com.tikitaka.naechinso.domain.sms.SmsCertificationServiceImpl;
//import org.hibernate.service.spi.InjectService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Profile;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@SpringBootTest(classes = SmsCertificationController.class)
//public class SmsCertificationControllerTest {
//
//    @Autowired
//    WebClient webClient;
//
//    @Autowired
//    RedisService redisService;
//    @Autowired
//    SmsCertificationService smsCertificationService;
//
//    @Value("${SPRING_PROFILE}")
//    String value;
//
//    @Test
//    @DisplayName("인자 유효성 검사 동작 확인")
//    public void testRequestSmsVerificationCodeValidation() {
//        System.out.println("mockWebClient = " + webClient);
//        System.out.println("mockRedisService = " + redisService);
//        System.out.println("smsCertificationService = " + smsCertificationService);
//        System.out.println("value = " + value);
//
//
//        //Mock Object DI
//
//        SmsCertificationController smsCertificationController
//                = new SmsCertificationController(smsCertificationService);
//
//        SmsVerificationCodeRequestDTO verificationCodeTestDto1
//                = new SmsVerificationCodeRequestDTO("010-1234-1234");
//
//        Assertions.assertThrows(MethodArgumentNotValidException.class, () -> {
//            smsCertificationController.sendMessageWithVerificationCode(verificationCodeTestDto1);
//        });
//    }
//}
