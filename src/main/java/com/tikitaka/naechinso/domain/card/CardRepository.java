package com.tikitaka.naechinso.domain.card;

import com.tikitaka.naechinso.domain.card.dto.CardResponseDTO;
import com.tikitaka.naechinso.domain.card.entity.Card;
import com.tikitaka.naechinso.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    int countByIsActive(boolean isActive);

    List<Card> findAllByMember(Member member);

    @Query("select new com.tikitaka.naechinso.domain.card.dto.CardResponseDTO(c.targetId, c.isActive, c.createdAt) " +
            "from Card c " +
            "join c.member m " +
            "where m = :member")
    List<CardResponseDTO> findAllDTOByMember(Member member);

    @Query("select new com.tikitaka.naechinso.domain.card.dto.CardResponseDTO(c.targetId, c.isActive, c.createdAt) " +
            "from Card c " +
            "join c.member m")
    List<CardResponseDTO> findAllDTO();


}
