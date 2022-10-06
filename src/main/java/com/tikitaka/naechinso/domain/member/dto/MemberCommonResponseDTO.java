package com.tikitaka.naechinso.domain.member.dto;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.constant.Role;
import com.tikitaka.naechinso.domain.member.entity.Member;
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
public class MemberCommonResponseDTO {

    private String phone;

    private Role role;

    private String name;

    private Gender gender;

    private int age;

    public static MemberCommonResponseDTO of(Member member) {
        MemberCommonResponseDTO res = MemberCommonResponseDTO.builder()
                .phone(member.getPhone())
                .role(member.getRole())
                .name(member.getName())
                .gender(member.getGender())
                .age(member.getAge())
                .build();

        return res;
    }
}
