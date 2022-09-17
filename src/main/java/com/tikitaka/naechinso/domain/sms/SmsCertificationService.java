package com.tikitaka.naechinso.domain.sms;

import com.tikitaka.naechinso.domain.sms.dto.SmsCertificationRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface SmsCertificationService {
    String makeSignature(Long currentTime);

    String sendVerificationMessage(String to);

    String verifyCode(SmsCertificationRequestDTO smsCertificationRequestDto);
}
