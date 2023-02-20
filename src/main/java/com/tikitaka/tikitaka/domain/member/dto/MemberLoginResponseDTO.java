package com.tikitaka.tikitaka.domain.member.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MemberLoginResponseDTO {
    private String fcmToken;
}
