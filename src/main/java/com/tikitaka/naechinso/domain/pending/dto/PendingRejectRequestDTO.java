package com.tikitaka.naechinso.domain.pending.dto;

import com.tikitaka.naechinso.domain.pending.constant.PendingType;
import com.tikitaka.naechinso.domain.pending.entity.Pending;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PendingRejectRequestDTO {
    @ApiModelProperty(example = "개못씀")
    @NotEmpty(message = "요청을 거부하는 이유를 작성해야 합니다")
    private String reason;

    @ApiModelProperty(example = "[\"img1.png\"]")
    private List<String> rejectImages;
}
