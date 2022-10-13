package com.tikitaka.naechinso.domain.match.dto;

import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendListResponseDTO;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendResponseDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MatchListResponseDTO {
    //내가 보낸 호감
    List<MatchResponseDTO> matchesTo = new ArrayList<>();
    //내가 받은 호감
    List<MatchResponseDTO> matchesFrom = new ArrayList<>();
    //남은 랜덤 매칭 카드 횟수
//    matching card


    public static MatchListResponseDTO of(Member member) {
        List<MatchResponseDTO> matchesToList = new ArrayList<>();
        List<MatchResponseDTO> matchesFromList = new ArrayList<>();

        if (member.getMatchesTo() != null) {
            matchesToList = member.getMatchesTo().stream().map(MatchResponseDTO::of).collect(Collectors.toList());
        }

        if (member.getMatchesFrom() != null) {
            matchesFromList = member.getMatchesFrom().stream().map(MatchResponseDTO::of).collect(Collectors.toList());
        }

        return MatchListResponseDTO.builder()
                .matchesFrom(matchesFromList)
                .matchesTo(matchesToList)
                .build();
    }


}
