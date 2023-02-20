package com.tikitaka.tikitaka.domain.member.dto;

import com.tikitaka.tikitaka.domain.member.constant.Gender;
import com.tikitaka.tikitaka.global.annotation.Enum;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MemberUpdateCommonRequestDTO {
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
}
