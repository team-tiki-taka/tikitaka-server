package com.tikitaka.naechinso.domain.point.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Positive;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class PointChargeRequestDTO {
    @ApiModelProperty(example = "5000")
    @Positive
    int value;
}
