package com.tikitaka.naechinso.domain.card.dto;

import com.tikitaka.naechinso.domain.card.entity.Card;
import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.dto.MemberOppositeProfileResponseDTO;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class CardThumbnailResponseDTO {
    private Long targetMemberId;
    private Boolean isActive;
    private String createdAt;

    private List<String> images;
    private String name;
    private int age;

    private String address;
    private Gender gender;

    private String jobName;
    private String jobPart;
    private String jobLocation;

    private String eduName;
    private String eduMajor;
    private String eduLevel;

    private String appeal;


    public static CardThumbnailResponseDTO of(Card card, Member targetMember) {
        if (targetMember == null) {
            throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        //상대 프로필 dto 에서 가져옴
        MemberOppositeProfileResponseDTO dto = MemberOppositeProfileResponseDTO.of(targetMember);

        return CardThumbnailResponseDTO.builder()
                .targetMemberId(card.getTargetMemberId())
                .isActive(card.getIsActive())
                .createdAt(card.getCreatedAt().toString())
                .images(targetMember.getDetail().getImages())
                .name(dto.getName())
                .age(dto.getAge())
                .address(dto.getAddress())
                .jobName(dto.getJobName())
                .jobPart(dto.getJobPart())
                .jobLocation(dto.getJobLocation())
                .eduName(dto.getEduName())
                .eduMajor(dto.getEduMajor())
                .eduLevel(dto.getEduLevel())
                .gender(dto.getGender())
                .appeal(dto.getAppeal())
                .build();
    }
}
