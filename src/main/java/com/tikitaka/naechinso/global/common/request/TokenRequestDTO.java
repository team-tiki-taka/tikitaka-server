package com.tikitaka.naechinso.global.common.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** JWT Token 요청 Dto
 * @author gengminy (220917)
 */
@Getter
@ToString
@NoArgsConstructor
public class TokenRequestDTO {
    private String accessToken;
    private String refreshToken;
}
