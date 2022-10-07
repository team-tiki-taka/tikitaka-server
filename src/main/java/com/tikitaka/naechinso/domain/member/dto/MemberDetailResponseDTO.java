package com.tikitaka.naechinso.domain.member.dto;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.constant.Role;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.member.entity.MemberDetail;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import lombok.*;

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

    private String personality;

    private String introduce;

    private String hobby;

    private String style;

    private String picture;

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
        MemberDetailResponseDTOBuilder dtoBuilder = MemberDetailResponseDTO.builder();
        Member member = detail.getMember();

        //멤버 정보가 연결되어 있으면 가져옴
        if (member != null) {
            dtoBuilder.phone(member.getPhone())
                    .role(member.getRole())
                    .name(member.getName())
                    .gender(member.getGender())
                    .age(member.getAge());

        } else {
            //없으면 에러
            throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        return dtoBuilder
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
                .build();
    }

}
