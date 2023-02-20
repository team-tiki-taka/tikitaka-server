package com.tikitaka.tikitaka.domain.card.dto;

import com.tikitaka.tikitaka.domain.card.entity.Card;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class CardResponseDTO {
    private Long targetMemberId;
    private Boolean isActive;
    private String createdAt;

    public static CardResponseDTO of(Card card) {
        return CardResponseDTO.builder()
                .targetMemberId(card.getTargetMemberId())
                .isActive(card.getIsActive())
                .createdAt(card.getCreatedAt().toString())
                .build();
    }

    public CardResponseDTO(Long id, Boolean isActive, LocalDateTime createdAt){
        this.targetMemberId = id;
        this.isActive = isActive;
        this.createdAt = createdAt.toString();
    }
}
