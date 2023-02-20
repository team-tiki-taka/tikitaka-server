package com.tikitaka.tikitaka.domain.match.dto;

import com.tikitaka.tikitaka.domain.match.constant.MatchStatus;
import com.tikitaka.tikitaka.domain.match.entity.Match;
import com.tikitaka.tikitaka.domain.member.constant.Gender;
import com.tikitaka.tikitaka.domain.member.dto.MemberOppositeProfileResponseDTO;
import com.tikitaka.tikitaka.domain.member.entity.Member;
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
    }

    public static MatchThumbnailResponseDTO of(Match match, Member member) {
        Member targetMember;

        //member 에 해당하는 상대의 정보를 가져옴
        if (member.equals(match.getFromMember())) {
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
                .image(dto.getImages() != null ? dto.getImages().get(0) : null)
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
                        .build())
                .build();
    }

    private static long generateDueDate(LocalDateTime createdAt) {
        LocalDateTime endDatetime = LocalDateTime.of(createdAt.toLocalDate().plusDays(3), LocalTime.of(23,59,59));
        long dueDate = Duration.between(LocalDateTime.now(), endDatetime).toDays();
        if (dueDate < 0) {
            return 0;
        }
        return dueDate;
    }
}
