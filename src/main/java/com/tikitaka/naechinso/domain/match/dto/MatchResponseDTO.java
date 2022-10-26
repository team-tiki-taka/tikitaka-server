package com.tikitaka.naechinso.domain.match.dto;

import com.tikitaka.naechinso.domain.match.constant.MatchStatus;
import com.tikitaka.naechinso.domain.match.entity.Match;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.BadRequestException;
import com.tikitaka.naechinso.global.error.exception.InternalServerException;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import lombok.*;
import org.joda.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MatchResponseDTO {

    private Long id;

    private MatchStatus status;

    private Long targetMemberId;

    public static MatchResponseDTO of(Match match) {
        if (match.getFromMember() == null || match.getToMember() == null) {
            throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
        }
        return MatchResponseDTO.builder()
                .id(match.getId())
                .status(match.getStatus())
                .targetMemberId(match.getToMember().getId())
                .build();
    }
}
