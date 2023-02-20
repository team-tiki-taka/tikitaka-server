package com.tikitaka.tikitaka.domain.recommend.dto;

import com.tikitaka.tikitaka.domain.member.constant.Gender;
import com.tikitaka.tikitaka.domain.recommend.entity.Recommend;
import lombok.*;

import java.util.List;

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

    private int age;

    private String meet;

    private List<String> appeals;

    private String appealDetail;

    private String period;

    private String senderName;

    private Long senderId;

    private Long receiverId;


    public static RecommendResponseDTO of(Recommend recommend) {
        Long senderId;
        String senderName;
        Long receiverId;
        if (recommend.getSender() == null) {
            senderId = null;
            senderName = null;
        } else {
            senderId = recommend.getSender().getId();
            senderName = recommend.getSenderName();
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
                .senderName(senderName)
                .receiverId(receiverId)
                .name(recommend.getReceiverName())
                .gender(recommend.getReceiverGender())
                .age(recommend.getReceiverAge())
                .meet(recommend.getReceiverMeet())
                .appeals(recommend.getReceiverAppeals())
                .appealDetail(recommend.getReceiverAppealDetail())
                .period(recommend.getReceiverPeriod())
                .build();
    }
}
