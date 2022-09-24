package com.tikitaka.naechinso.domain.member.dto;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.entity.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 추천인 및 추천 받는 사람 공통 가입을 위한 Dto입니다
 * @author gengminy 220924
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MemberCommonJoinResponseDto {
    private String phone;

    private String name;

    private Gender gender;

    private int age;

    public static MemberCommonJoinResponseDto of(Member member) {
        MemberCommonJoinResponseDto res = MemberCommonJoinResponseDto.builder()
                .phone(member.getPhone())
                .name(member.getName())
                .gender(member.getGender())
                .age(member.getAge())
                .build();

        return res;
    }
}
