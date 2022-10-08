package com.tikitaka.naechinso.domain.member.dto;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.global.annotation.Enum;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * 추천 받은 사람의 가입을 위한 Dto입니다
 * @author gengminy 220924
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MemberDetailJoinRequestDTO {

    @ApiModelProperty(example = "닉")
    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @ApiModelProperty(example = "M")
    @Enum(enumClass = Gender.class, message = "성별 입력이 올바르지 않습니다. M 또는 W가 필요합니다")
    private Gender gender;

    @ApiModelProperty(example = "25")
    @Min(value = 25, message = "25-35세까지만 가입 가능합니다")
    @Max(value = 35, message = "25-35세까지만 가입 가능합니다")
    private int age;

    @ApiModelProperty(example = "180")
    @Positive(message = "키는 양수여야 합니다")
    private int height;

    @ApiModelProperty(example = "서울시 강남구")
    @NotBlank(message = "주소 정보를 입력해야 합니다")
    private String address;

    @NotBlank(message = "종교 정보를 입력해야 합니다")
    private String religion;

    @NotBlank(message = "음주 정보를 입력해야 합니다")
    private String drink;

    @NotBlank(message = "흡연 정보를 입력해야 합니다")
    private String smoke;

    @ApiModelProperty(example = "ESTJ")
    @Length(max = 4, message = "올바른 MBTI 정보를 입력하세요")
    private String mbti;

    @NotBlank(message = "성격 정보를 입력해야 합니다")
    private String personality;

    @NotBlank(message = "자기 소개를 입력해야 합니다")
    private String introduce;

    @NotBlank(message = "취미 정보를 입력해야 합니다")
    private String hobby;

    @NotBlank(message = "연애 스타일을 입력해야 합니다")
    private String style;

    @NotBlank(message = "사진을 업로드해야 합니다")
    private String picture;

}
