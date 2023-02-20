package com.tikitaka.tikitaka.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tikitaka.tikitaka.domain.recommend.dto.RecommendReceiverDTO;
import lombok.*;

import java.util.List;

/**
 * 토큰 재발급을 담당하는 응답 DTO
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL) //NULL 필드 가림
public class MemberReissueResponseDTO {
    private String accessToken;
    private String refreshToken;

    //내 번호로 추천받은 정보 리스트
    private List<RecommendReceiverDTO> recommendReceived;
    //가입 완료했는지 여부, detail 정보가 있으면 true
    private Boolean isActive = false;
    //서버로부터 차단 당했는지 여부
    private Boolean isBanned = false;
}
