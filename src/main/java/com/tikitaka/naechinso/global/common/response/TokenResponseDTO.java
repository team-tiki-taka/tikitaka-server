package com.tikitaka.naechinso.global.common.response;

import lombok.*;

/** JWT Token 응답 Dto
 * @author gengminy (220728) */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponseDTO {
    private String accessToken;
    private String refreshToken;
}
