package com.tikitaka.tikitaka.domain.sms;

import com.tikitaka.tikitaka.domain.sms.dto.SmsCertificationSuccessResponseDTO;
import com.tikitaka.tikitaka.global.config.CommonApiResponse;
import com.tikitaka.tikitaka.domain.sms.dto.SmsCertificationRequestDTO;
import com.tikitaka.tikitaka.domain.sms.dto.SmsVerificationCodeRequestDTO;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "인증번호가 담긴 문자를 보낸다")
    public CommonApiResponse<String> sendMessageWithVerificationCode(@Valid  @RequestBody SmsVerificationCodeRequestDTO dto) {
        String result = smsCertificationService.sendVerificationMessage(dto.getPhoneNumber());
        return CommonApiResponse.of(result);
    }

    @PostMapping("/verify")
    @ApiOperation(value = "인증번호를 입력하여 인증한다")
    public CommonApiResponse<SmsCertificationSuccessResponseDTO> verifyCode(@RequestBody @Valid SmsCertificationRequestDTO dto) {
        SmsCertificationSuccessResponseDTO responseDTO = smsCertificationService.verifyCode(dto);
        return CommonApiResponse.of(responseDTO);
    }
}
