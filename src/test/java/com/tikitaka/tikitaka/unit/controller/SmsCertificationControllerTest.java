package com.tikitaka.tikitaka.unit.controller;

import com.tikitaka.tikitaka.domain.sms.SmsCertificationController;
import com.tikitaka.tikitaka.domain.sms.SmsCertificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
