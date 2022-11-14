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

import java.util.Objects;

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

    /** member 가 아닌 상대방을 타겟으로 아이디를 가져옴 */
    public static MatchResponseDTO of(Match match, Member member) {
        Long targetMemberId;

        if (match.getFromMember() == null || match.getToMember() == null) {
            throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        if (Objects.equals(match.getFromMember().getId(), member.getId())) {
            targetMemberId = match.getToMember().getId();
        } else if (Objects.equals(match.getToMember().getId(), member.getId())){
            targetMemberId = match.getFromMember().getId();
        } else {
            throw new BadRequestException(ErrorCode.BAD_MATCH_STATUS);
        }

        return MatchResponseDTO.builder()
                .id(match.getId())
                .status(match.getStatus())
                .targetMemberId(targetMemberId)
                .build();
    }
}
