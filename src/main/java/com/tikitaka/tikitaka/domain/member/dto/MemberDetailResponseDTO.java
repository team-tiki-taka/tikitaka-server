package com.tikitaka.tikitaka.domain.member.dto;

import com.tikitaka.tikitaka.domain.member.constant.Gender;
import com.tikitaka.tikitaka.domain.member.constant.Role;
import com.tikitaka.tikitaka.domain.member.entity.Member;
import com.tikitaka.tikitaka.domain.member.entity.MemberDetail;
import com.tikitaka.tikitaka.global.error.ErrorCode;
import com.tikitaka.tikitaka.global.error.exception.NotFoundException;
import lombok.*;

import java.util.List;

/**
 * 추천인 및 추천 받는 사람 공통 가입을 위한 Dto입니다
 * @author gengminy 220924
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MemberDetailResponseDTO {

    //추천인 정보
    private String phone;

    private Role role;

    private String name;

    private Gender gender;

    private int age;

    private int height;

    private String address;

    private String religion;

    private String drink;

    private String smoke;

    private String mbti;

    private List<String> personalities;

    private String introduce;

    private String hobby;

    private String style;

    private List<String> images;

    private Long point;

    public static MemberDetailResponseDTO of(Member member) {
        MemberDetail detail = member.getDetail();
        //회원가입 정보가 없으면 null
        if (detail == null) {
            return null;
        }
        return buildDetail(detail);
    }

    public static MemberDetailResponseDTO of(MemberDetail memberDetail) {
        return buildDetail(memberDetail);
    }

    private static MemberDetailResponseDTO buildDetail(MemberDetail detail) {
        Member member = detail.getMember();

        //멤버 정보가 없으면 에러
        if (member == null) {
            throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        return MemberDetailResponseDTO.builder()
                .phone(member.getPhone())
                .role(member.getRole())
                .name(member.getName())
                .gender(member.getGender())
                .age(member.getAge())
                .height(detail.getHeight())
                .address(detail.getAddress())
                .religion(detail.getReligion())
                .drink(detail.getDrink())
                .smoke(detail.getSmoke())
                .mbti(detail.getMbti())
                .personalities(detail.getPersonalities())
                .introduce(detail.getIntroduce())
                .hobby(detail.getHobby())
                .style(detail.getStyle())
                .images(detail.getImages())
                .point(0L)
                .build();
    }

}
