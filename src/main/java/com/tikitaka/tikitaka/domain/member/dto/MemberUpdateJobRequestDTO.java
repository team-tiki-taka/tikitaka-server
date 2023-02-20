package com.tikitaka.tikitaka.domain.member.dto;

import com.tikitaka.tikitaka.global.error.ErrorCode;
import com.tikitaka.tikitaka.global.error.exception.BadRequestException;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.json.JSONObject;

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
public class MemberUpdateJobRequestDTO {
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

    public static MemberUpdateJobRequestDTO of(String content) {
        JSONObject json = new JSONObject(content);
        try {
            final String jobName = json.getString("jobName");
            final String jobLocation = json.getString("jobLocation");
            final String jobPart = json.getString("jobPart");
            final String jobImage = json.getString("jobImage");
            return MemberUpdateJobRequestDTO.builder()
                    .jobName(jobName)
                    .jobPart(jobPart)
                    .jobLocation(jobLocation)
                    .jobImage(jobImage)
                    .build();
        } catch (Exception e) {
            throw new BadRequestException(ErrorCode._BAD_REQUEST, "JSON 형식 오류입니다");
        }
    }
}
