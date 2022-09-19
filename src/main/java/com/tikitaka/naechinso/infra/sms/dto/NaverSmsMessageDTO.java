package com.tikitaka.naechinso.infra.sms.dto;

import lombok.*;

/**
 * 메세지 전송을 위한 DTO
 * @author gengminy 22.09.10.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class NaverSmsMessageDTO {
    String to;
    String content;
}
