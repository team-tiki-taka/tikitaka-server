package com.tikitaka.naechinso.unit.controller;

import com.tikitaka.naechinso.domain.sms.SmsCertificationController;
import com.tikitaka.naechinso.domain.sms.dto.SmsVerificationCodeRequestDTO;
import com.tikitaka.naechinso.global.config.redis.RedisService;
import com.tikitaka.naechinso.domain.sms.SmsCertificationService;
import com.tikitaka.naechinso.domain.sms.SmsCertificationServiceImpl;
import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.reactive.function.client.WebClient;

@ExtendWith(MockitoExtension.class)
public class SmsCertificationControllerTest {

    @InjectMocks
    private SmsCertificationController smsCertificationController;
    @Mock
    private SmsCertificationService smsCertificationService;
    //HTTP Method 호출용 MockMvc
    private MockMvc mockMvc;
    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(smsCertificationController).build();

    }

    @Value("${spring.profiles.active}")
    String value;

    @Test
    @DisplayName("인자 유효성 검사 동작 확인")
    public void testRequestSmsVerificationCodeValidation() {

    }
}
