package com.tikitaka.naechinso.domain.card;

import com.tikitaka.naechinso.domain.card.dto.CardResponseDTO;
import com.tikitaka.naechinso.domain.card.dto.CardThumbnailResponseDTO;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
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
     * @// TODO: 2022/10/22 조건에 맞는 타겟 멤버 중에서 랜덤 선택하도록
     * */
    public CardThumbnailResponseDTO createRandomCard(Member authMember) {
        //이미 ACTIVE 한 카드가 있으면 에러
        if (cardRepository.existsByMemberAndIsActiveTrue(authMember)) {
            throw new BadRequestException(ErrorCode.ACTIVE_CARD_ALREADY_EXIST);
        }
        //하루에 세 장 이상 추천을 요청할 경우 에러
        if (countCardByMemberAndCreatedAtBetween(authMember) >= 3) {
            throw new BadRequestException(ErrorCode.CARD_LIMIT_EXCEED);
        }

        Member member = memberRepository.findByMember(authMember)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        Gender memberGender = member.getGender();

        //이미 존재하는 카드에 담긴 유저 ID들 가져오기
        List<Long> existTargetMemberIds = member.getCards().stream().map(Card::getTargetMemberId).collect(Collectors.toList());
        existTargetMemberIds.add(member.getId());

        //추천 받았던 적 없는 새로운 추천 상대 리스트
        List<Member> newTargetMemberList = memberRepository.findByIdNotInAndGenderNotAndDetailNotNull(existTargetMemberIds, memberGender);
        if (newTargetMemberList.isEmpty()) {
            throw new NotFoundException(ErrorCode.RANDOM_USER_NOT_FOUND);
        }
        //(0 ~ size) 사이의 랜덤 인덱스 멤버 추출
        Member newTargetMember = newTargetMemberList.get(new Random().nextInt(newTargetMemberList.size()));

        Card newCard = Card.builder()
                .member(member)
                .targetMemberId(newTargetMember.getId())
                .isActive(true)
                .build();

        cardRepository.save(newCard);

        return CardThumbnailResponseDTO.of(newCard, newTargetMember);
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

    /**
     * 오늘 날짜를 기준으로
     * */
    private long countCardByMemberAndCreatedAtBetween(Member member) {
        //서버 시간으로 00시00분부터 23시59분까지받은 카드 수 종합
        LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
        LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));
        return cardRepository.countByMemberAndCreatedAtBetween(member, startDatetime, endDatetime);
    }
}
