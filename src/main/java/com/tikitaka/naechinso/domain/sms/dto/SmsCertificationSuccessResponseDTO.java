package com.tikitaka.naechinso.domain.sms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendReceiverDTO;
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

    //내 번호로 추천받은 정보 리스트
    private List<RecommendReceiverDTO> recommendReceived = new ArrayList<>();
    //가입 완료했는지 여부, detail 정보가 있으면 true
    private Boolean isActive = false;
    //서버로부터 차단 당했는지 여부
    private Boolean isBanned = false;
}
