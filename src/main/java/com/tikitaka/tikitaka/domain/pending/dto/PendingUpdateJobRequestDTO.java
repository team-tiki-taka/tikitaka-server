package com.tikitaka.tikitaka.domain.pending.dto;

import com.tikitaka.tikitaka.domain.member.dto.MemberUpdateJobRequestDTO;
import com.tikitaka.tikitaka.domain.pending.constant.PendingType;
import com.tikitaka.tikitaka.global.annotation.Enum;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;

/**
 * 유저의 직장 정보나 학교 인증 정보를 업데이트 할 때
 * 어드민의 승인을 받기 위한 Pending 엔티티 생성을 요청하는 DTO
 * @author gengminy 221008
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PendingUpdateJobRequestDTO {
    @ApiModelProperty(example = "1")
    @Positive(message = "올바르지 않은 id 형식입니다")
    private Long memberId;

    @ApiModelProperty(example = "job")
    @Enum(enumClass = PendingType.class, message = "승인 요청 타입이 올바르지 않습니다")
    private PendingType type;

    @ApiModelProperty
    @NotEmpty
    private MemberUpdateJobRequestDTO content;

    @ApiModelProperty(example = "img1.png")
    @NotEmpty
    private String image;
}
