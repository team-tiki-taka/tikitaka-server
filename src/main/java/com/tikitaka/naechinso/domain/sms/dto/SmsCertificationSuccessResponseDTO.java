package com.tikitaka.naechinso.domain.sms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendResponseDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL) //NULL 필드 가림
public class SmsCertificationSuccessResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String registerToken;

    //추천 받았는지 여부, true 면 유효한 유저
    private Boolean recommendReceived = false;
}
