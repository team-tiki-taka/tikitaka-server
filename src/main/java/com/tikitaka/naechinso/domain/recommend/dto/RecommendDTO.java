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

    private Long senderId;

    private Long receiverId;

    private String name;

    private Gender gender;

    private String meet;

    private String appeal;


    public static RecommendDTO of(Recommend recommend) {
        Long senderId;
        Long receiverId;
        if (recommend.getSender() == null) {
            senderId = null;
        } else {
            senderId = recommend.getSender().getId();
        }

        if (recommend.getReceiver() == null) {
            receiverId = null;
        } else {
            receiverId = recommend.getReceiver().getId();
        }

        return RecommendDTO.builder()
                .phone(recommend.getReceiverPhone())
                .senderId(senderId)
                .receiverId(receiverId)
                .name(recommend.getReceiverName())
                .gender(recommend.getReceiverGender())
                .meet(recommend.getReceiverMeet())
                .appeal(recommend.getReceiverAppeal())
                .build();
    }
}
