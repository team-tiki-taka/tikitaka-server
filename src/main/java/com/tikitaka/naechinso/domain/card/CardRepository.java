package com.tikitaka.naechinso.domain.card;

import com.tikitaka.naechinso.domain.card.dto.CardResponseDTO;
import com.tikitaka.naechinso.domain.card.entity.Card;
import com.tikitaka.naechinso.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    int countByIsActive(boolean isActive);

    List<Card> findAllByMember(Member member);

    Optional<Card> findByMemberAndIsActiveTrue(Member member);
    Boolean existsByMemberAndIsActiveTrue(Member member);

    long countByMemberAndCreatedAtBetween(Member member, LocalDateTime start, LocalDateTime end);

    List<Card> findAllByIsActiveTrue();

    @Query("select c.targetMemberId " +
            "from Card c " +
            "where c.targetMemberId <> :id")
    List<Long> findTargetIdsByIdNot(Long id);

    @Query("select new com.tikitaka.naechinso.domain.card.dto.CardResponseDTO(c.targetMemberId, c.isActive, c.createdAt) " +
            "from Card c " +
            "join c.member m " +
            "where m = :member")
    List<CardResponseDTO> findAllDTOByMember(Member member);

    @Query("select new com.tikitaka.naechinso.domain.card.dto.CardResponseDTO(c.targetMemberId, c.isActive, c.createdAt) " +
            "from Card c " +
            "join c.member m")
    List<CardResponseDTO> findAllDTO();


}
