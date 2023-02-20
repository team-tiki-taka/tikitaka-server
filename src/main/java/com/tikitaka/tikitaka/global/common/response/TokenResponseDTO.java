package com.tikitaka.tikitaka.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/** JWT Token 응답 Dto
 * @author gengminy (220728) */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) //NULL 필드 가림
public class TokenResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String registerToken;
    private Boolean recommendReceived;
}
