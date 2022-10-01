package com.tikitaka.naechinso.domain.recommend.dto;

import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.recommend.entity.Recommend;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RecommendListResponseDTO {
    List<RecommendDTO> recommend = new ArrayList<>();
    List<RecommendDTO> recommendReceived = new ArrayList<>();


    public static RecommendListResponseDTO of(Member member) {
        List<RecommendDTO> recommendList = new ArrayList<>();
        List<RecommendDTO> recommendReceivedList = new ArrayList<>();

        member.getRecommends().stream().forEach(recommend ->
                recommendList.add(RecommendDTO.of(recommend))
        );

        member.getRecommend_received().stream().forEach(recommend ->
                recommendReceivedList.add(RecommendDTO.of(recommend))
        );

        return RecommendListResponseDTO.builder()
                .recommend(recommendList)
                .recommendReceived(recommendReceivedList)
                .build();
    }


}
