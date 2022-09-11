package com.tikitaka.naechinso.service;

import com.tikitaka.naechinso.dto.SmsCertificationRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface SmsCertificationService {
    String makeSignature(Long currentTime);

    String sendVerificationMessage(String to);

    String verifyCode(SmsCertificationRequestDTO smsCertificationRequestDto);
}
