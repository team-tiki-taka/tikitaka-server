package com.tikitaka.naechinso.domain.member.dto;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.member.entity.MemberDetail;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 추천인 및 추천 받는 사람 공통 가입을 위한 Dto입니다
 * @author gengminy 220924
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MemberDetailResponseDto {

//    private Member member;
    //추천인 정보
    private String phone;

    private String name;

    private Gender gender;

    private int age;



    private int height;

    private String address;

    private String religion;

    private String drink;

    private String smoke;

    private String mbti;

    private String personality;

    private String introduce;

    private String hobby;

    private String style;

    private String picture;

    private Long point;

    private String school;

    private String major;

    private String eduLevel;

    public static MemberDetailResponseDto of(Member member) {
        MemberDetail detail = member.getDetail();
        //회원가입 정보가 없으면 null
        if (detail == null) {
            return null;
        }
        return buildDetail(detail);
    }

    public static MemberDetailResponseDto of(MemberDetail memberDetail) {
        return buildDetail(memberDetail);
    }

    private static MemberDetailResponseDto buildDetail(MemberDetail detail) {
        return MemberDetailResponseDto.builder()
                .height(detail.getHeight())
                .address(detail.getAddress())
                .religion(detail.getReligion())
                .drink(detail.getDrink())
                .smoke(detail.getSmoke())
                .mbti(detail.getMbti())
                .personality(detail.getPersonality())
                .introduce(detail.getIntroduce())
                .hobby(detail.getHobby())
                .style(detail.getStyle())
                .picture(detail.getPicture())
                .point(detail.getPoint())
                .school(detail.getSchool())
                .major(detail.getMajor())
                .eduLevel(detail.getEduLevel())
                .build();
    }

}
