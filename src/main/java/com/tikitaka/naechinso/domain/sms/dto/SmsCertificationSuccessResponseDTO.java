package com.tikitaka.naechinso.domain.sms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendDTO;
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

    //receiver 는 있지만 sender 는 없는 Recommend 를 가져옴 (추천 요청)
    @Builder.Default
    private List<RecommendDTO> recommendRequests = new ArrayList<>();

    //추천 받았는지 여부, true 면 유효한 유저
    private Boolean recommendReceived = false;
}
