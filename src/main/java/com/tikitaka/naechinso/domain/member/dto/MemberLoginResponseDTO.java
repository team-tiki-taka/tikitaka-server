package com.tikitaka.naechinso.domain.member.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MemberLoginResponseDTO {
    private String fcmToken;
}
