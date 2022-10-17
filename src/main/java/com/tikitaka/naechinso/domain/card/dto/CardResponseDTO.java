package com.tikitaka.naechinso.domain.card.dto;

import com.tikitaka.naechinso.domain.card.entity.Card;
import com.tikitaka.naechinso.domain.match.constant.MatchStatus;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class CardResponseDTO {
    private Long targetId;
    private Boolean isActive;
    private String createdAt;

    public static CardResponseDTO of(Card card) {
        return CardResponseDTO.builder()
                .targetId(card.getTargetId())
                .isActive(card.getIsActive())
                .createdAt(card.getCreatedAt().toString())
                .build();
    }

    public CardResponseDTO(Long id, Boolean isActive, LocalDateTime createdAt){
        this.targetId = id;
        this.isActive = isActive;
        this.createdAt = createdAt.toString();
    }
}
