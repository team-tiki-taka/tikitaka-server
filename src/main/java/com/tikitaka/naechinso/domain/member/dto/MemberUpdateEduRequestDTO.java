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
public class MemberUpdateEduRequestDTO {
    @ApiModelProperty(example = "서울")
    @NotBlank(message = "학교명을 입력해주세요")
    private String eduName;

    @ApiModelProperty(example = "컴퓨터공학과")
    @NotBlank(message = "전공을 입력해주세요")
    private String eduMajor;

    @ApiModelProperty(example = "대학교")
    @NotBlank(message = "최종학력을 입력해주세요")
    private String eduLevel;

    @ApiModelProperty(example = "인증 사진 링크")
    @NotBlank(message = "인증 사진을 업로드 해주세요")
    private String eduImage;
}
