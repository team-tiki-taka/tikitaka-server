package com.tikitaka.naechinso.domain.match.dto;

import com.tikitaka.naechinso.domain.card.dto.CardThumbnailResponseDTO;
import com.tikitaka.naechinso.domain.card.entity.Card;
import com.tikitaka.naechinso.domain.match.constant.MatchStatus;
import com.tikitaka.naechinso.domain.match.entity.Match;
import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.dto.MemberOppositeProfileResponseDTO;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MatchThumbnailResponseDTO {
    private Long id;
    private Long targetMemberId;
    private MatchStatus status;
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

    public static MatchThumbnailResponseDTO of(Match match, Member member) {
        Member targetMember;

        //매칭 주체에 따라 상대의 정보를 가져옴
        if (member.equals(match.getToMember())) {
            System.out.println("member.equals(match.getToMember()) = " + member.equals(match.getToMember()));
            targetMember = match.getFromMember();
        } else {
            targetMember = match.getToMember();
        }

        MemberOppositeProfileResponseDTO dto = MemberOppositeProfileResponseDTO.of(targetMember);
        return MatchThumbnailResponseDTO.builder()
                .id(match.getId())
                .targetMemberId(targetMember.getId())
                .status(match.getStatus())
                .createdAt(match.getCreatedAt().toString())
                .dueDate(generateDueDate(match.getCreatedAt()))
                .images(dto.getImages())
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
        if (dueDate < 0) {
            return 0;
        }
        return dueDate;
    }
}
