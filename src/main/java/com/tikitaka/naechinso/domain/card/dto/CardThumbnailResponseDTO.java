package com.tikitaka.naechinso.domain.card.dto;

import com.tikitaka.naechinso.domain.card.entity.Card;
import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.dto.MemberOppositeProfileResponseDTO;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.recommend.entity.Recommend;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import lombok.*;

import java.time.*;
import java.util.Date;
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

    @Builder.Default
    private long dueDate = 0L;

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

    private Recommendation recommend;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @ToString
    private static class Recommendation {
        private String name;
        private Gender gender;
        private String appeal;

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

        return CardThumbnailResponseDTO.builder()
                .targetMemberId(card.getTargetMemberId())
                .isActive(card.getIsActive())
                .createdAt(card.getCreatedAt().toString())
                .dueDate(generateDueDate(card.getCreatedAt()))
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
                .recommend(Recommendation.builder()
                        .name(dto.getRecommend().getName())
                        .gender(dto.getRecommend().getGender())
                        .appeal(dto.getRecommend().getAppeal())
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

    private static long generateDueDate(LocalDateTime createdAt) {
        LocalDateTime endDatetime = LocalDateTime.of(createdAt.toLocalDate().plusDays(7), LocalTime.of(23,59,59));
        long dueDate = Duration.between(LocalDateTime.now(), endDatetime).toDays();

        System.out.println("endDatetime = " + endDatetime);

        if (dueDate < 0) {
            return 0;
        }
        return dueDate;
    }
}
