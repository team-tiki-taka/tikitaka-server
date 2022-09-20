package com.tikitaka.naechinso.domain;

import com.tikitaka.naechinso.domain.sms.dto.SmsVerificationCodeRequestDTO;
import com.tikitaka.naechinso.global.config.CommonApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainController {
    @GetMapping("/")
    public CommonApiResponse<Boolean> rootEndPoint() {
        return CommonApiResponse.of(true);
    }
}
