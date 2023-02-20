package com.tikitaka.tikitaka.domain.recommend.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.util.List;

/** 내친소 회원이 다른 유저를 추천할 때 사용할 DTO
 * @author gengminy 221003
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RecommendMemberAcceptRequestDTO {
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
