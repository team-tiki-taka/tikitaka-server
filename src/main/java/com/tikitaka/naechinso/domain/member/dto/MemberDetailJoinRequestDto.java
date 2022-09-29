package com.tikitaka.naechinso.domain.member.dto;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.entity.Member;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
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
public class MemberDetailJoinRequestDto {
    //추천인 정보
    //
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
    @Length(max = 4, message = "올바를 MBTI 정보를 입력하세요")
    private String mbti = "";

    @NotBlank(message = "성격 정보를 입력해야 합니다")
    private String personality;

    @NotBlank(message = "자기 소개를 입력해야 합니다")
    private String introduce = "";

    @NotBlank(message = "취미 정보를 입력해야 합니다")
    private String hobby = "";

    @NotBlank(message = "연애 스타일을 입력해야 합니다")
    private String style = "";

    @NotBlank(message = "사진을 업로드해야 합니다")
    private String picture;

    @ApiModelProperty(example = "서울")
    private String school;

    @ApiModelProperty(example = "컴퓨터공학과")
    private String major;

    @ApiModelProperty(example = "대학교")
    private String eduLevel;


    public static Member toCommonMember(MemberDetailJoinRequestDto dto) {
        Member member = Member.builder()

                .build();

        return member;
    }
}
