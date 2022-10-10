package com.tikitaka.naechinso.domain.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 직업 정보 업데이트를 위한 DTO
 * @author gengminy 221005
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MemberUpdateImageRequestDTO {
    @ApiModelProperty(example = "[\"img1\", \"img2\", \"img3\"]")
    @Size(min = 3, max = 3, message = "사진 3장을 업로드해야 합니다")
    @NotNull
    private List<String> images;

        public static MemberUpdateImageRequestDTO of(List<String> images) {
        return new MemberUpdateImageRequestDTO(images);
    }
}
