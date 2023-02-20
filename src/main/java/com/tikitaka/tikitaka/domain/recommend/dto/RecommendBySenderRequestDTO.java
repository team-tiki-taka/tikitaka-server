package com.tikitaka.tikitaka.domain.recommend.dto;

import com.tikitaka.tikitaka.domain.member.constant.Gender;
import com.tikitaka.tikitaka.global.annotation.Enum;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.util.List;

/** 가입하지 않은 멤버가 추천사를 써줄 때 요청 DTO
 * @author gengminy 221001
*/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RecommendBySenderRequestDTO {
    @ApiModelProperty(example = "01099999999")
    @NotBlank(message = "친구의 전화번호를 입력해주세요")
    @Pattern(regexp = "[0-9]{10,11}", message = "하이픈 없는 10~11자리 숫자를 입력해주세요")
    private String phone;

    @ApiModelProperty(example = "박스")
    @NotBlank(message = "친구의 이름을 입력해주세요")
    private String name;

    @ApiModelProperty(example = "M")
    @Enum(enumClass = Gender.class, message = "친구의 성별 입력이 올바르지 않습니다. M 또는 W가 필요합니다")
    private Gender gender;

    @ApiModelProperty(example = "1998")
    @Min(value = 1988, message = "1988-1998 년생까지만 가입 가능합니다")
    @Max(value = 1998, message = "1988-1998 년생까지만 가입 가능합니다")
    private int age;

    @ApiModelProperty(example = "CMC 에서")
    @NotBlank(message = "만나게 된 계기를 입력해주세요")
    private String meet;

    @ApiModelProperty(example = "[\"패션센스 \uD83E\uDDE5\", \"사랑꾼 \uD83D\uDC97\", \"애교쟁이 \uD83D\uDE18\"]")
    @Size(min = 3, max = 3, message = "매력 키워드 3개를 입력해야 합니다")
    @NotNull
    private List<String> appeals;

    @ApiModelProperty(example = "짱짱짜짱 멋진 개발자입니다")
    @NotBlank(message = "친구의 자세한 매력을 입력해주세요")
    private String appealDetail;

    @ApiModelProperty(example = "1년")
    @NotBlank(message = "만난 기간을 입력해주세요")
    private String period;
}
