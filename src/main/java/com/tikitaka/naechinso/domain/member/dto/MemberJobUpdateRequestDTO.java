package com.tikitaka.naechinso.domain.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * 직업 정보 업데이트를 위한 DTO
 * @author gengminy 221005
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MemberJobUpdateRequestDTO {
    @ApiModelProperty(example = "카카오")
    @NotBlank(message = "직장명을 입력해주세요")
    private String jobName;

    @ApiModelProperty(example = "개발자")
    @NotBlank(message = "직장 부서를 입력해주세요")
    private String jobPart;

    @ApiModelProperty(example = "판교")
    @NotBlank(message = "직장 위치를 입력해주세요")
    private String jobLocation;

    @ApiModelProperty(example = "인증 사진 링크")
    @NotBlank(message = "인증 사진을 업로드 해주세요")
    private String jobImage;
}
