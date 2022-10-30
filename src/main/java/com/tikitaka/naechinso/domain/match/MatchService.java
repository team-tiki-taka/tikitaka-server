package com.tikitaka.naechinso.domain.match;

import com.tikitaka.naechinso.domain.card.CardRepository;
import com.tikitaka.naechinso.domain.card.dto.CardOppositeMemberProfileResponseDTO;
import com.tikitaka.naechinso.domain.card.entity.Card;
import com.tikitaka.naechinso.domain.match.constant.MatchStatus;
import com.tikitaka.naechinso.domain.match.dto.*;
import com.tikitaka.naechinso.domain.match.entity.Match;
import com.tikitaka.naechinso.domain.match.event.MatchLikeEvent;
import com.tikitaka.naechinso.domain.match.event.MatchOpenEvent;
import com.tikitaka.naechinso.domain.match.event.MatchResponseEvent;
import com.tikitaka.naechinso.domain.member.MemberRepository;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.BadRequestException;
import com.tikitaka.naechinso.global.error.exception.ForbiddenException;
import com.tikitaka.naechinso.global.error.exception.InternalServerException;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MatchService {

    private final MemberRepository memberRepository;
    private final MatchRepository matchRepository;
    private final CardRepository cardRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

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

        //알림 이벤트 전송
        try {
            applicationEventPublisher.publishEvent(new MatchLikeEvent(authMember, targetMember));
        } catch (Exception e) {
            log.error("푸시 알림 전송에 실패했습니다 - {}", e.getMessage());
        }

        return MatchResponseDTO.of(match);
    }

    /**
     * 호감을 보낸 상대를 거절한다
     * @param authMember
     * @return
     */
    public MatchResponseDTO reject(Member authMember, Long matchId) {
        Match match = matchRepository.findByIdAndToMemberAndStatus(matchId, authMember, MatchStatus.PENDING)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MATCH_NOT_FOUND));

        Member fromMember = match.getFromMember();

        match.reject();
        matchRepository.save(match);

        //알림 이벤트 전송
        if (fromMember.isAcceptsPush()) {
            try {
                MatchResponseEvent event = new MatchResponseEvent(fromMember, authMember, false);
                applicationEventPublisher.publishEvent(event);

            } catch (Exception e) {
                log.error("푸시 알림 전송에 실패했습니다 - {}", e.getMessage());
            }
        }

        return MatchResponseDTO.of(match);
    }

    /**
     * 호감을 보낸 상대를 수락한다
     * @param authMember
     * @return
     */
    public MatchResponseDTO accept(Member authMember, Long matchId) {
        Match match = matchRepository.findByIdAndToMemberAndStatus(matchId, authMember, MatchStatus.PENDING)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MATCH_NOT_FOUND));

        Member fromMember = match.getFromMember();

        match.accept();
        matchRepository.save(match);

        //알림 이벤트 전송
        if (fromMember.isAcceptsPush()) {
            try {
                MatchResponseEvent event = new MatchResponseEvent(fromMember, authMember, true);
                applicationEventPublisher.publishEvent(event);

            } catch (Exception e) {
                log.error("푸시 알림 전송에 실패했습니다 - {}", e.getMessage());
            }
        }

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

        //알림 이벤트 전송
        try {
            Member opposite = authMember.equals(match.getFromMember()) ? match.getToMember() : match.getFromMember();
            applicationEventPublisher.publishEvent(new MatchOpenEvent(authMember, opposite));
        } catch (Exception e) {
            log.error("푸시 알림 전송에 실패했습니다 - {}", e.getMessage());
        }

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


    /**
     * 호감 주거나 받은 상대의 프로필 정보를 가져옴
     * */
    public MatchBasicProfileResponseDTO getBasicProfileById(Member authMember, Long id) {
        //관련 매칭 정보가 없으면 403
        if(!matchRepository.existsByTargetId(id)){
            throw new ForbiddenException(ErrorCode.FORBIDDEN_PROFILE);
        }

        Member oppositeMember = memberRepository.findByMember(authMember)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        return MatchBasicProfileResponseDTO.of(oppositeMember);
    }

    /**
     * 호감 주거나 받은 상대의 프로필 정보를 가져옴
     * */
    public MatchOpenProfileResponseDTO getOpenProfileById(Member authMember, Long id) {
        //OPEN 상태인 매칭 정보가 없다면 에러
        if(!matchRepository.existsByTargetIdAndStatusIsOpen(id)){
            throw new ForbiddenException(ErrorCode.FORBIDDEN_PROFILE);
        }

        Member oppositeMember = memberRepository.findByMember(authMember)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        return MatchOpenProfileResponseDTO.of(oppositeMember);
    }

    public MatchListResponseDTO findAllByMember(Member authMember) {
        return MatchListResponseDTO.of(authMember);
    }
}
