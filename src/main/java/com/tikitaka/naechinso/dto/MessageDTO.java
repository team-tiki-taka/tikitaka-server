package com.tikitaka.naechinso.dto;

import lombok.*;

/**
 * 메세지 전송을 위한 DTO
 * @author gengminy 22.09.10.
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MessageDTO {
    String to;
    String content;
}
