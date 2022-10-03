package com.tikitaka.naechinso.domain.recommend.dto;

import com.tikitaka.naechinso.domain.member.entity.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RecommendListResponseDTO {
    List<RecommendResponseDTO> recommend = new ArrayList<>();
    List<RecommendResponseDTO> recommendReceived = new ArrayList<>();


    public static RecommendListResponseDTO of(Member member) {
        List<RecommendResponseDTO> recommendList = new ArrayList<>();
        List<RecommendResponseDTO> recommendReceivedList = new ArrayList<>();

        member.getRecommends().stream().forEach(recommend ->
                recommendList.add(RecommendResponseDTO.of(recommend))
        );

        member.getRecommend_received().stream().forEach(recommend ->
                recommendReceivedList.add(RecommendResponseDTO.of(recommend))
        );

        return RecommendListResponseDTO.builder()
                .recommend(recommendList)
                .recommendReceived(recommendReceivedList)
                .build();
    }


}
