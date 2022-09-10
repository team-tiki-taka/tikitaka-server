package com.tikitaka.naechinso.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class ResponseMessageDTO {
    String requestId;
    LocalDateTime requestTime;
    String statusCode;
    String statusName;
}
