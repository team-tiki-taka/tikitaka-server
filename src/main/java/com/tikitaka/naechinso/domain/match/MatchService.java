package com.tikitaka.naechinso.domain.match;

import com.tikitaka.naechinso.domain.card.CardRepository;
import com.tikitaka.naechinso.domain.card.CardService;
import com.tikitaka.naechinso.domain.card.entity.Card;
import com.tikitaka.naechinso.domain.match.constant.MatchStatus;
import com.tikitaka.naechinso.domain.match.dto.MatchListResponseDTO;
import com.tikitaka.naechinso.domain.match.dto.MatchResponseDTO;
import com.tikitaka.naechinso.domain.match.dto.MatchThumbnailResponseDTO;
import com.tikitaka.naechinso.domain.match.entity.Match;
import com.tikitaka.naechinso.domain.member.MemberRepository;
import com.tikitaka.naechinso.domain.member.MemberService;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.BadRequestException;
import com.tikitaka.naechinso.global.error.exception.ForbiddenException;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MatchService {

    private final MemberRepository memberRepository;
    private final MatchRepository matchRepository;
    private final CardRepository cardRepository;

    /**
     * 활성화된 카드에 해당하는 상대에게 호감을 보낸다
     * @param authMember
     * @return
     */
    public MatchResponseDTO like(Member authMember) {
        Card activeCard = cardRepository.findByMemberAndIsActiveTrue(authMember)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ACTIVE_CARD_NOT_FOUND));

        Member targetMember = memberRepository.findById(activeCard.getTargetMemberId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Match match = Match.builder()
                .status(MatchStatus.PENDING)
                .fromMember(authMember)
                .toMember(targetMember)
                .build();

        //카드 비활성화
        activeCard.disable();

        matchRepository.save(match);
        cardRepository.save(activeCard);

        return MatchResponseDTO.of(match);
    }


    /**
     * 활성화된 카드에 해당하는 상대에게 호감을 보낸다
     * @param authMember
     * @return
     */
    public MatchResponseDTO accept(Member authMember, Long matchId) {
        Match match = matchRepository.findByIdAndToMemberAndStatus(matchId, authMember,MatchStatus.PENDING)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MATCH_NOT_FOUND));

        match.accept();
        matchRepository.save(match);
        return MatchResponseDTO.of(match);
    }

    /**
     * 번호 오픈권을 사용하여 번호를 오픈한다
     * @param authMember
     * @return
     */
    public MatchResponseDTO openPhone(Member authMember, Long matchId) {
        Match match = matchRepository.findByIdAndStatus(matchId, MatchStatus.ACCEPTED)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MATCH_NOT_FOUND));

        //유저에게 속하지 않은 매칭에 대한 유효하지 않은 요청
        if (!match.getFromMember().getId().equals(authMember.getId())
            && !match.getToMember().getId().equals(authMember.getId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_MATCH);
        }

        match.open();
        matchRepository.save(match);

        return MatchResponseDTO.of(match);
    }


    /**
     * 호감을 받은 매칭 정보를 모두 가져온다
     * @param authMember
     * @return
     */
    public List<MatchThumbnailResponseDTO> findAllByToMember(Member authMember) {
        return matchRepository.findAllByToMemberNotComplete(authMember).stream()
                .map(match -> MatchThumbnailResponseDTO.of(match, authMember))
                .collect(Collectors.toList());
    }

    /**
     * 호감을 보낸 매칭 정보를 모두 가져온다
     * @param authMember
     * @return
     */
    public List<MatchThumbnailResponseDTO> findAllByFromMember(Member authMember) {
        return matchRepository.findAllByFromMemberNotComplete(authMember).stream()
                .map(match -> MatchThumbnailResponseDTO.of(match, authMember))
                .collect(Collectors.toList());
    }

    /**
     * 호감을 서로 보내어 성사 완료된 매칭 정보를 모두 가져온다
     * @param authMember
     * @return
     */
    public List<MatchThumbnailResponseDTO> findAllByMatchComplete(Member authMember) {
        return matchRepository.findAllByMemberComplete(authMember).stream()
                .map(match -> MatchThumbnailResponseDTO.of(match, authMember))
                .collect(Collectors.toList());
    }


    public MatchListResponseDTO findAllByMember(Member authMember) {
        return MatchListResponseDTO.of(authMember);
    }
}
