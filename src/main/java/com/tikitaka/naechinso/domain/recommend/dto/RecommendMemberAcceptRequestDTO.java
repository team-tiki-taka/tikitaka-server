package com.tikitaka.naechinso.domain.recommend.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;

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

    @ApiModelProperty(example = "최고")
    @NotBlank(message = "친구의 성격 키워드를 입력해주세요")
    private String personality;

    @ApiModelProperty(example = "짱")
    @NotBlank(message = "친구의 매력 키워드를 입력해주세요")
    private String appeal;

    @ApiModelProperty(example = "짱짱짜짱 멋진 개발자입니다")
    @NotBlank(message = "친구의 자세한 매력을 입력해주세요")
    private String appealDetail;

    @ApiModelProperty(example = "1년")
    @NotBlank(message = "만난 기간을 입력해주세요")
    private String period;
}
