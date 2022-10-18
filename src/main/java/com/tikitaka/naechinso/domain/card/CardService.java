package com.tikitaka.naechinso.domain.card;

import com.tikitaka.naechinso.domain.card.dto.CardResponseDTO;
import com.tikitaka.naechinso.domain.card.entity.Card;
import com.tikitaka.naechinso.domain.member.MemberRepository;
import com.tikitaka.naechinso.domain.member.MemberService;
import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.BadRequestException;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;

//    public CardResponseDTO createCard(Member authMember) {
//
//        Member member = memberRepository.findByMember(authMember)
//                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
//
//        Card newCard = Card.builder()
//                .member(member)
//                .targetId(3L)
//                .build();
//
//        cardRepository.save(newCard);
//
//        return CardResponseDTO.of(newCard);
//    }

    /**
     * 추천 받았던 유저를 필터링한 랜덤 추천 카드를 만든다
     * */
    public CardResponseDTO createRandomCard(Member authMember) {
        //아직 ACTIVE 한 카드가 있으면 에러
        if (cardRepository.existsByMemberAndIsActiveTrue(authMember)) {
            throw new BadRequestException(ErrorCode.ACTIVE_CARD_ALREADY_EXIST);
        }

        Member member = memberRepository.findByMember(authMember)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        Gender memberGender = member.getGender();

        //이미 존재하는 카드에 담긴 유저 ID들
        List<Long> existTargetMemberIds = member.getCards().stream().map(Card::getTargetId).collect(Collectors.toList());
        existTargetMemberIds.add(member.getId());

        //새로운 추천 상대
        Member newTargetMember = memberRepository.findTopByIdNotInAndGenderNotAndDetailNotNull(existTargetMemberIds, memberGender)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RANDOM_USER_NOT_FOUND));

        Card newCard = Card.builder()
                .member(member)
                .target(newTargetMember)
                .targetId(newTargetMember.getId())
                .isActive(true)
                .build();

        cardRepository.save(newCard);

        return CardResponseDTO.of(newCard);
    }


    /**
     * 현재 ACTIVE 한 카드를 모두 거절하고 INACTIVE 상태로 만든다
     * */
    public CardResponseDTO rejectCard(Member authMember) {
        Card activeCard = findByMemberAndIsActiveTrue(authMember);
        activeCard.disable();

        cardRepository.save(activeCard);

        return CardResponseDTO.of(activeCard);
    }

    public List<CardResponseDTO> findAllDTOByMember(Member member) {
        return cardRepository.findAllDTOByMember(member);
    }

    public List<CardResponseDTO> findAllDTO() {
        return cardRepository.findAllDTO();
    }

    public Card findByMemberAndIsActiveTrue(Member member) {
        return cardRepository.findByMemberAndIsActiveTrue(member)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ACTIVE_CARD_NOT_FOUND));
    }
}
