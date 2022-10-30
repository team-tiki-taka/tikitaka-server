package com.tikitaka.naechinso.domain.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MemberLoginRequestDTO {
    @ApiModelProperty(example = "asdsad")
    @NotBlank(message = "FCM Token 정보를 입력해야 합니다")
    private String fcmToken;
}
