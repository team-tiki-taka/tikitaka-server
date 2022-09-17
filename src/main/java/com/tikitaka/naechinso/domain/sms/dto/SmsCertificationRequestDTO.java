package com.tikitaka.naechinso.domain.sms.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class SmsCertificationRequestDTO {

    @NotBlank(message = "전화번호를 입력해주세요")
    @Pattern(regexp = "[0-9]{10,11}", message = "하이픈 없는 10~11자리 숫자를 입력해주세요")
    String phoneNumber;

    @NotBlank(message = "인증번호를 입력해주세요")
    @Pattern(regexp = "[0-9]{6}", message = "인증번호 6자리를 입력해주세요")
    String code;
}
