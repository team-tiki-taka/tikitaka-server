package com.tikitaka.naechinso.controller;

import com.tikitaka.naechinso.config.CommonApiResponse;
import com.tikitaka.naechinso.dto.SmsCertificationRequestDTO;
import com.tikitaka.naechinso.dto.SmsVerificationCodeRequestDTO;
import com.tikitaka.naechinso.service.SmsCertificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsCertificationController {
    private final SmsCertificationService smsCertificationService;

    @PostMapping("/send")
    public CommonApiResponse<String> sendMessageWithVerificationCode( @Valid  @RequestBody SmsVerificationCodeRequestDTO dto) {
        String result = smsCertificationService.sendVerificationMessage(dto.getPhoneNumber());
        return CommonApiResponse.of(result);
    }

    @PostMapping("/verify")
    public CommonApiResponse<String> verifyCode(@RequestBody @Valid SmsCertificationRequestDTO dto) {
        String result = smsCertificationService.verifyCode(dto);
        return CommonApiResponse.of(result);
    }
}
