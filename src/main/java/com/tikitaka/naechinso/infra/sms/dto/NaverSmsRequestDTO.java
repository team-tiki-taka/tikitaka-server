package com.tikitaka.naechinso.infra.sms.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class NaverSmsRequestDTO {
    String type;
    String contentType;
    String countryCode;
    String from;
    String content;
    List<NaverSmsMessageDTO> messages;
}
