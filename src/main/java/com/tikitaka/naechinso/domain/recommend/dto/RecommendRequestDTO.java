package com.tikitaka.naechinso.domain.recommend.dto;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.global.annotation.Enum;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RecommendRequestDTO {
    @NotBlank(message = "친구의 전화번호를 입력해주세요")
    @Pattern(regexp = "[0-9]{10,11}", message = "하이픈 없는 10~11자리 숫자를 입력해주세요")
    private String phone;

    @NotBlank(message = "친구의 이름을 입력해주세요")
    private String name;

    @Enum(enumClass = Gender.class, message = "친구의 성별 입력이 올바르지 않습니다. M 또는 W가 필요합니다")
    private Gender gender;

    @Min(value = 25, message = "25-33세까지만 추천 및 가입 가능합니다")
    @Max(value = 33, message = "25-33세까지만 추천 및 가입 가능합니다")
    private int age;

    @NotBlank(message = "만나게 된 계기를 입력해주세요")
    private String meet;

    @NotBlank(message = "친구의 성격 키워드를 입력해주세요")
    private String personality;

    @NotBlank(message = "친구의 매력을 입력해주세요")
    private String appeal;
}
