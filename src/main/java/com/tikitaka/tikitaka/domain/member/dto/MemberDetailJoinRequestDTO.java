package com.tikitaka.tikitaka.domain.member.dto;

import com.tikitaka.tikitaka.domain.member.constant.Gender;
import com.tikitaka.tikitaka.global.annotation.Enum;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.List;

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

    @ApiModelProperty(example = "1998")
    @Min(value = 1988, message = "1988-1998 년생까지만 가입 가능합니다")
    @Max(value = 1998, message = "1988-1998 년생까지만 가입 가능합니다")
    private int age;

    @ApiModelProperty(example = "180")
    @Positive(message = "키는 양수여야 합니다")
    private int height;

    @ApiModelProperty(example = "서울시 강남구")
    @NotBlank(message = "주소 정보를 입력해야 합니다")
    private String address;

    @ApiModelProperty(example = "무교")
    @NotBlank(message = "종교 정보를 입력해야 합니다")
    private String religion;

    @ApiModelProperty(example = "1병")
    @NotBlank(message = "음주 정보를 입력해야 합니다")
    private String drink;

    @ApiModelProperty(example = "비흡연자")
    @NotBlank(message = "흡연 정보를 입력해야 합니다")
    private String smoke;

    @ApiModelProperty(example = "ESTJ")
    @Length(max = 4, message = "올바른 MBTI 정보를 입력하세요")
    private String mbti;

    @ApiModelProperty(example = "[\"열정적인 \uD83D\uDD25\", \"지적인 \uD83E\uDDD0\", \"상냥한 ☺️\"]")
    @Size(min = 3, max = 3, message = "성격 키워드 3개를 입력해야 합니다")
    @NotNull
    private List<String> personalities;

    @ApiModelProperty(example = "반갑습니다")
    @NotBlank(message = "자기 소개를 입력해야 합니다")
    private String introduce;

    @ApiModelProperty(example = "코딩")
    @NotBlank(message = "취미 정보를 입력해야 합니다")
    private String hobby;

    @ApiModelProperty(example = "헌신적임")
    @NotBlank(message = "연애 스타일을 입력해야 합니다")
    private String style;

    @ApiModelProperty(example = "[\"img1\", \"img2\", \"img3\"]")
    @Size(min = 3, max = 3, message = "사진 3장을 업로드해야 합니다")
    @NotNull
    private List<String> images;
}
