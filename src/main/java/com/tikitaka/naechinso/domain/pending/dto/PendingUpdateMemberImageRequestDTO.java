package com.tikitaka.naechinso.domain.pending.dto;

import com.tikitaka.naechinso.domain.pending.constant.PendingType;
import com.tikitaka.naechinso.global.annotation.Enum;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 유저 프로필 사진을 업로드 할 때
 * 어드민의 승인을 받기 위한 Pending 엔티티 생성을 요청하는 DTO
 * @author gengminy 221008
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PendingUpdateMemberImageRequestDTO {
    @ApiModelProperty(example = "1")
    @Positive(message = "올바르지 않은 id 형식입니다")
    private Long memberId;

    @ApiModelProperty(example = "member")
    @Enum(enumClass = PendingType.class, message = "승인 요청 타입이 올바르지 않습니다")
    private PendingType type;

    @ApiModelProperty(example = "[\"img1.png\", \"img2.jpg\", \"img3.png\"]")
    @Size(min = 3, max = 3, message = "사진 3장을 업로드해야 합니다")
    @NotNull
    private List<String> images;
}
