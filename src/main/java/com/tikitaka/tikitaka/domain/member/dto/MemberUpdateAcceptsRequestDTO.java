package com.tikitaka.tikitaka.domain.member.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * 직업 정보 업데이트를 위한 DTO
 * @author gengminy 221005
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MemberUpdateAcceptsRequestDTO {
    @NotNull(message = "위치 정보 제공 동의 여부가 필요합니다")
    private boolean acceptsLocation;

    @NotNull(message = "마케팅 동의 여부가 필요합니다")
    private boolean acceptsMarketing;

    @NotNull(message = "푸시 알림 동의 여부가 필요합니다")
    private boolean acceptsPush;
}
