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
    List<RecommendDTO> recommends = new ArrayList<>();


    public static RecommendListResponseDTO of(Member member) {
        List<RecommendDTO> recommendDTOList = new ArrayList<>();

        member.getRecommends().stream().forEach(recommend ->
            recommendDTOList.add(RecommendDTO.of(recommend))
        );

        return RecommendListResponseDTO.builder()
                .recommends(recommendDTOList)
                .build();
    }
}
