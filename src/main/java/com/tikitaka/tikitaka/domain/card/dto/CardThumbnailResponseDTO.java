package com.tikitaka.tikitaka.domain.card.dto;

import com.tikitaka.tikitaka.domain.card.entity.Card;
import com.tikitaka.tikitaka.domain.member.constant.Gender;
import com.tikitaka.tikitaka.domain.member.dto.MemberOppositeProfileResponseDTO;
import com.tikitaka.tikitaka.domain.member.entity.Member;
import com.tikitaka.tikitaka.global.error.ErrorCode;
import com.tikitaka.tikitaka.global.error.exception.NotFoundException;
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

    private String image;
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

    private Recommendation recommend;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @ToString
    private static class Recommendation {
        private String name;
        private Gender gender;
        private List<String> appeals;

        private String jobName;
        private String jobPart;
        private String jobLocation;
        private String eduName;
        private String eduMajor;
        private String eduLevel;

        private String meet;
        private String period;
        private String appealDetail;
    }

    public static CardThumbnailResponseDTO of(Card card, Member targetMember) {
        if (targetMember == null) {
            throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        //상대 프로필 dto 에서 가져옴
        MemberOppositeProfileResponseDTO dto = MemberOppositeProfileResponseDTO.of(targetMember);
        String topImage = targetMember.getDetail().getImages() != null ? targetMember.getDetail().getImages().get(0) : null;

        return CardThumbnailResponseDTO.builder()
                .targetMemberId(card.getTargetMemberId())
                .isActive(card.getIsActive())
                .createdAt(card.getCreatedAt().toString())
                .image(topImage)
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
                .recommend(Recommendation.builder()
                        .name(dto.getRecommend().getName())
                        .gender(dto.getRecommend().getGender())
                        .appeals(dto.getRecommend().getAppeals())
                        .jobName(dto.getRecommend().getJobName())
                        .jobLocation(dto.getRecommend().getJobLocation())
                        .jobPart(dto.getRecommend().getJobPart())
                        .eduName(dto.getRecommend().getEduName())
                        .eduMajor(dto.getRecommend().getEduMajor())
                        .eduLevel(dto.getRecommend().getEduLevel())
                        .meet(dto.getRecommend().getMeet())
                        .period(dto.getRecommend().getPeriod())
                        .appealDetail(dto.getRecommend().getAppealDetail())
                        .build())
                .build();
    }

}
