package com.tikitaka.naechinso.domain.recommend.dto;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.recommend.entity.Recommend;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RecommendDTO {

    private String phone;

    private Member sender;

    private Member receiver;

    private String name;

    private Gender gender;

    private String meet;

    private String appeal;


    public static RecommendDTO of(Recommend recommend) {
        return RecommendDTO.builder()
                .phone(recommend.getPhone())
                .sender(recommend.getSender())
                .receiver(recommend.getReceiver())
                .name(recommend.getName())
                .gender(recommend.getGender())
                .meet(recommend.getMeet())
                .appeal(recommend.getAppeal())
                .build();
    }
}
