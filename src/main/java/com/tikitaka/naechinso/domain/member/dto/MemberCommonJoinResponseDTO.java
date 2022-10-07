package com.tikitaka.naechinso.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.annotation.Enum;
import com.tikitaka.naechinso.global.common.response.TokenResponseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;

/**
 * 임시회원의 가입 정보를 리턴하는 DTO 입니다
 * @author gengminy 221006
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL) //NULL 필드 가림
public class MemberCommonJoinResponseDTO {

    private String phone;
    private String name;
    private Gender gender;
    private int age;

    private String accessToken;
    private String refreshToken;

    public static MemberCommonJoinResponseDTO of(Member member, TokenResponseDTO tokenDTO) {
        return MemberCommonJoinResponseDTO.builder()
                .phone(member.getPhone())
                .name(member.getName())
                .gender(member.getGender())
                .age(member.getAge())
                .accessToken(tokenDTO.getAccessToken())
                .refreshToken(tokenDTO.getRefreshToken())
                .build();
    }

    public static MemberCommonJoinResponseDTO of(Member member) {
        return MemberCommonJoinResponseDTO.builder()
                .phone(member.getPhone())
                .name(member.getName())
                .gender(member.getGender())
                .age(member.getAge())
                .build();
    }
}
