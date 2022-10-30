package com.tikitaka.naechinso.domain.card.dto;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.member.entity.MemberDetail;
import com.tikitaka.naechinso.domain.recommend.entity.Recommend;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import com.tikitaka.naechinso.global.util.CustomStringUtil;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class CardOppositeMemberProfileResponseDTO {

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

    private String personality;
    private String religion;
    private int height;
    private String smoke;
    private String drink;

    private String hobby;
    private String style;
    private String introduce;

    private String mbti;
    private Recommendation recommend;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @ToString
    public static class Recommendation {
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

        public static Recommendation of(Recommend recommend) {
            Member sender = recommend.getSender();
            return Recommendation.builder()
                    .name(CustomStringUtil.hideName(recommend.getSenderName()))
                    .gender(recommend.getSenderGender())
                    .appeal(recommend.getReceiverAppeal())
                    .appealDetail(recommend.getReceiverAppealDetail())
                    .eduName(sender.getEduName())
                    .eduMajor(sender.getEduMajor())
                    .eduLevel(sender.getEduLevel())
                    .jobName(sender.getJobName())
                    .jobPart(sender.getJobPart())
                    .jobLocation(sender.getJobLocation())
                    .meet(recommend.getReceiverMeet())
                    .period(recommend.getReceiverPeriod())
                    .build();
        }
    }

    public static CardOppositeMemberProfileResponseDTO of(Member member) {
        MemberDetail detail = member.getDetail();
        //디테일 없는 유저로 시도
        if (detail == null) {
            throw new NotFoundException(ErrorCode.USER_NOT_SIGNED_UP);
        }

        //추천받은 적 없는 유저를 가져오는 것을 시도
        if (member.getRecommendReceived() == null){
            throw new NotFoundException(ErrorCode.RECOMMEND_NOT_FOUND);
        }

        for (Recommend recommend: member.getRecommendReceived()) {
            //추천 상태 검증
            if (recommend.getSender() != null && recommend.getReceiver() != null) {
                return CardOppositeMemberProfileResponseDTO.builder()
                        .images(detail.getImages())
                        .name(CustomStringUtil.hideName(member.getName()))
                        .age(member.getAge())
                        .address(detail.getAddress())
                        .jobName(member.getJobName())
                        .jobPart(member.getJobPart())
                        .jobLocation(member.getJobLocation())
                        .eduName(member.getEduName())
                        .eduMajor(member.getEduMajor())
                        .eduLevel(member.getEduLevel())
                        .gender(member.getGender())
                        .personality(detail.getPersonality())
                        .religion(detail.getReligion())
                        .height(detail.getHeight())
                        .smoke(detail.getSmoke())
                        .drink(detail.getDrink())
                        .hobby(detail.getHobby())
                        .style(detail.getStyle())
                        .introduce(detail.getIntroduce())
                        .mbti(detail.getMbti())
                        .recommend(CardOppositeMemberProfileResponseDTO.Recommendation.of(recommend))
                        .build();

            }
        }
        throw new NotFoundException(ErrorCode.RECOMMEND_NOT_FOUND);
    }
}
