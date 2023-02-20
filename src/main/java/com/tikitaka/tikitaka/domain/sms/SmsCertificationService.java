package com.tikitaka.tikitaka.domain.sms;

import com.tikitaka.tikitaka.domain.sms.dto.SmsCertificationRequestDTO;
import com.tikitaka.tikitaka.domain.sms.dto.SmsCertificationSuccessResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface SmsCertificationService {
    String sendVerificationMessage(String to);
    SmsCertificationSuccessResponseDTO verifyCode(SmsCertificationRequestDTO smsCertificationRequestDto);
}
