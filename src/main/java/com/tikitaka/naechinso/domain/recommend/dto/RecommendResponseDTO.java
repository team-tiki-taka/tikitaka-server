package com.tikitaka.naechinso.domain.recommend.dto;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.recommend.entity.Recommend;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RecommendResponseDTO {

    private String uuid;

    private String phone;

    private String name;

    private Gender gender;

    private String meet;

    private String appeal;

    private String appealDetail;

    private String period;

    private Long senderId;

    private Long receiverId;


    public static RecommendResponseDTO of(Recommend recommend) {
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

        return RecommendResponseDTO.builder()
                .uuid(recommend.getUuid())
                .phone(recommend.getReceiverPhone())
                .senderId(senderId)
                .receiverId(receiverId)
                .name(recommend.getReceiverName())
                .gender(recommend.getReceiverGender())
                .meet(recommend.getReceiverMeet())
                .appeal(recommend.getReceiverAppeal())
                .appealDetail(recommend.getReceiverAppealDetail())
                .period(recommend.getReceiverPeriod())
                .build();
    }
}
