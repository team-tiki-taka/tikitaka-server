package com.tikitaka.naechinso.domain.card.dto;

import com.tikitaka.naechinso.domain.card.entity.Card;
import com.tikitaka.naechinso.domain.match.constant.MatchStatus;
import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.dto.MemberOppositeProfileResponseDTO;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.member.entity.MemberDetail;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.BadRequestException;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class CardResponseDTO {
    private Long targetId;
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


    public static CardResponseDTO of(Card card) {
        if (card.getTarget() == null) {
            throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        //상대 프로필 dto 에서 가져옴
        MemberOppositeProfileResponseDTO dto = MemberOppositeProfileResponseDTO.of(card.getTarget());

        return CardResponseDTO.builder()
                .targetId(card.getTargetId())
                .isActive(card.getIsActive())
                .createdAt(card.getCreatedAt().toString())
                .images(card.getTarget().getDetail().getImages())
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

    public CardResponseDTO(Long id, Boolean isActive, LocalDateTime createdAt){
        this.targetId = id;
        this.isActive = isActive;
        this.createdAt = createdAt.toString();
    }
}
