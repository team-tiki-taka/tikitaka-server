package com.tikitaka.naechinso.domain.recommend.dto;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.annotation.Enum;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.validation.constraints.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RecommendRequestDTO {
    @ApiModelProperty(example = "닉")
    @NotBlank(message = "유저 이름을 입력해주세요")
    private String name;

    @ApiModelProperty(example = "M")
    @Enum(enumClass = Gender.class, message = "유저의 성별 입력이 올바르지 않습니다. M 또는 W가 필요합니다")
    private Gender gender;

    @ApiModelProperty(example = "25")
    @Min(value = 25, message = "25-33세까지만 추천 및 가입 가능합니다")
    @Max(value = 33, message = "25-33세까지만 추천 및 가입 가능합니다")
    private int age;

    @NotNull(message = "서비스 이용약관 동의가 필요합니다")
    @AssertTrue(message = "서비스 이용약관 동의가 필요합니다")
    private boolean acceptsService;

    @NotNull(message = "개인정보 이용 동의가 필요합니다")
    @AssertTrue(message = "개인정보 이용 동의가 필요합니다")
    private boolean acceptsInfo;

    @NotNull(message = "종교 정보 제공 동의가 필요합니다")
    @AssertTrue(message = "종교 정보 제공 동의가 필요합니다")
    private boolean acceptsReligion;

    @NotNull(message = "위치 정보 제공 동의 여부가 필요합니다")
    private boolean acceptsLocation;

    @NotNull(message = "마케팅 동의 여부가 필요합니다")
    private boolean acceptsMarketing;

    public Member toReceiver(String phone) {
        return Member.builder()
                .name(this.getName())
                .name(this.name)
                .gender(this.gender)
                .age(this.age)
                .acceptsService(this.acceptsService)
                .acceptsInfo(this.acceptsInfo)
                .acceptsReligion(this.acceptsReligion)
                .acceptsLocation(this.acceptsLocation)
                .acceptsMarketing(this.acceptsMarketing)
                .build();
    }
}
