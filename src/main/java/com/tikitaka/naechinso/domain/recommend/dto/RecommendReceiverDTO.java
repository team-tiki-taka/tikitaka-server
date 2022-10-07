package com.tikitaka.naechinso.domain.recommend.dto;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.recommend.entity.Recommend;
import lombok.*;

/**
 * 유저 초기화면에서 반환할 받은 추천사 정보 DTO
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RecommendReceiverDTO {
    private String name;

    private Gender gender;

    private int age;

    public static RecommendReceiverDTO of(Recommend recommend) {
        return RecommendReceiverDTO.builder()
                .name(recommend.getReceiverName())
                .gender(recommend.getReceiverGender())
                .name(recommend.getReceiverName())
                .build();
    }
}
