package com.tikitaka.naechinso.domain.card;

import com.tikitaka.naechinso.domain.card.dto.CardResponseDTO;
import com.tikitaka.naechinso.domain.card.entity.Card;
import com.tikitaka.naechinso.domain.member.MemberService;
import com.tikitaka.naechinso.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final MemberService memberService;

    public CardResponseDTO createCard(Member authMember) {

        Member member = memberService.findByMember(authMember);

        Card newCard = Card.builder()
                .member(member)
                .targetId(3L)
                .build();

        cardRepository.save(newCard);

        return CardResponseDTO.of(newCard);
    }

    public List<CardResponseDTO> findAllByMember(Member member) {
        return cardRepository.findAllDTOByMember(member);
    }

    public List<CardResponseDTO> findAll() {
        return cardRepository.findAllDTO();
    }


}
