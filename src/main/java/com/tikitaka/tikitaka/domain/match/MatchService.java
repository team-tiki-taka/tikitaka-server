package com.tikitaka.tikitaka.domain.match;

import com.tikitaka.tikitaka.domain.card.CardRepository;
import com.tikitaka.tikitaka.domain.card.entity.Card;
import com.tikitaka.tikitaka.domain.match.constant.MatchStatus;
import com.tikitaka.tikitaka.domain.match.dto.*;
import com.tikitaka.tikitaka.domain.match.entity.Match;
import com.tikitaka.tikitaka.domain.match.event.MatchLikeEvent;
import com.tikitaka.tikitaka.domain.match.event.MatchOpenEvent;
import com.tikitaka.tikitaka.domain.match.event.MatchResponseEvent;
import com.tikitaka.tikitaka.domain.member.MemberRepository;
import com.tikitaka.tikitaka.domain.member.entity.Member;
import com.tikitaka.tikitaka.global.error.ErrorCode;
import com.tikitaka.tikitaka.global.error.exception.BadRequestException;
import com.tikitaka.tikitaka.global.error.exception.ForbiddenException;
import com.tikitaka.tikitaka.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    int EXPIRY_DATE_DAY = 3;

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

        return MatchResponseDTO.of(match, authMember);
    }

    /**
     * 호감을 보낸 상대를 거절한다
     * @param authMember
     * @return
     */
    public MatchResponseDTO reject(Member authMember, Long matchId) {
        Match match = matchRepository.findByIdAndToMemberAndStatus(matchId, authMember, MatchStatus.PENDING)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MATCH_NOT_FOUND));
        //만료된 매칭
        if (match.getIsExpired()) {
            throw new BadRequestException(ErrorCode.EXPIRED_MATCH);
        }

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

        return MatchResponseDTO.of(match, authMember);
    }

    /**
     * 호감을 보낸 상대를 수락한다
     * @param authMember
     * @return
     */
    public MatchResponseDTO accept(Member authMember, Long matchId) {
        Match match = matchRepository.findByIdAndToMemberAndStatus(matchId, authMember, MatchStatus.PENDING)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MATCH_NOT_FOUND));
        //만료된 매칭
        if (match.getIsExpired()) {
            throw new BadRequestException(ErrorCode.EXPIRED_MATCH);
        }

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

        return MatchResponseDTO.of(match, authMember);
    }

    /**
     * 번호 오픈권을 사용하여 번호를 오픈한다
     * @param authMember
     * @return
     */
    public MatchResponseDTO openPhone(Member authMember, Long matchId) {
        Match match = matchRepository.findByIdAndStatus(matchId, MatchStatus.ACCEPTED)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MATCH_NOT_FOUND));
        //만료된 매칭
        if (match.getIsExpired()) {
            throw new BadRequestException(ErrorCode.EXPIRED_MATCH);
        }

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

        return MatchResponseDTO.of(match, authMember);
    }


    /**
     * 호감 주거나 받은 상대의 프로필 정보를 가져옴
     * */
    public MatchBasicProfileResponseDTO getBasicProfileById(Member authMember, Long targetId) {
        //내정보 가져오기
        final Member member = memberRepository.findByMember(authMember)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        final Long memberId = member.getId();

        //관련 매칭 정보가 없으면 403
        final Match match = matchRepository.findByFromMemberIdAndToMemberIdAndIsExpiredFalse(memberId, targetId)
                .orElseThrow(() -> new ForbiddenException(ErrorCode.FORBIDDEN_PROFILE));

        if (match.getFromMember() != null && match.getFromMember() == member) {
            return MatchBasicProfileResponseDTO.of(match.getToMember());
        } else if (match.getToMember() != null && match.getToMember() == member){
            return MatchBasicProfileResponseDTO.of(match.getFromMember());
        } else {
            throw new BadRequestException(ErrorCode.BAD_MATCH_STATUS);
        }
    }

    /**
     * 호감 주거나 받은 상대의 프로필 정보를 가져옴
     * */
    public MatchOpenProfileResponseDTO getOpenProfileById(Member authMember, Long targetId) {
        final Member member = memberRepository.findByMember(authMember)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        final Long memberId = member.getId();

        //OPEN 상태인 매칭 정보가 없다면 에러
        final Match match = matchRepository.findByTargetIdAndIsExpiredFalseAndStatusIsOpen(memberId, targetId)
                .orElseThrow(() -> new ForbiddenException(ErrorCode.FORBIDDEN_PROFILE));

        if (match.getFromMember() != null && match.getFromMember() == member) {
            return MatchOpenProfileResponseDTO.of(match.getToMember());
        } else if (match.getToMember() != null && match.getToMember() == member){
            return MatchOpenProfileResponseDTO.of(match.getFromMember());
        } else {
            throw new BadRequestException(ErrorCode.BAD_MATCH_STATUS);
        }
    }

    /**
     * 호감을 받은 매칭 정보를 모두 가져온다
     * @param authMember
     * @return
     */
    public List<MatchThumbnailResponseDTO> findAllByToMember(Member authMember) {
        return matchRepository.findAllByToMemberNotComplete(authMember).stream()
                .map(match -> MatchThumbnailResponseDTO.of(match, match.getFromMember()))
                .collect(Collectors.toList());
    }

    /**
     * 호감을 보낸 매칭 정보를 모두 가져온다
     * @param authMember
     * @return
     */
    public List<MatchThumbnailResponseDTO> findAllByFromMember(Member authMember) {
        return matchRepository.findAllByFromMemberNotComplete(authMember).stream()
                .map(match -> MatchThumbnailResponseDTO.of(match, match.getToMember()))
                .collect(Collectors.toList());
    }

    /**
     * 호감을 서로 보내어 성사 완료된 매칭 정보를 모두 가져온다
     * @param authMember
     * @return
     */
    public List<MatchThumbnailResponseDTO> findAllByMatchComplete(Member authMember) {
        return matchRepository.findAllByMemberComplete(authMember).stream()
                .map(match -> {
                    //내가 보낸 호감의 상대
                    if (match.getFromMember().getId().equals(authMember.getId()))
                        return MatchThumbnailResponseDTO.of(match, match.getToMember());
                    //내가 받은 호감의 상대
                    else
                        return MatchThumbnailResponseDTO.of(match, match.getFromMember());
                })
                .collect(Collectors.toList());
    }

    public MatchListResponseDTO findAllByMember(Member authMember) {
        return MatchListResponseDTO.of(authMember);
    }


    /**
     * 매일 00시00분 마다 기간이 지난 카드 자동 만료
     */
    @Scheduled(cron = "0 0 0 * * ?") //daily at 00:00
    protected void scheduleTask() {
        //현재 시간으로 부터 3일 전의 00시 00분 이전 시각
        LocalDateTime compareDateTime = LocalDateTime.of(LocalDate.now().minusDays(EXPIRY_DATE_DAY), LocalTime.of(0,0,0));
        //그 이전의 카드는 만료되었으므로 모두 가져옴
        List<Match> matchList = matchRepository.findAllByIsExpiredFalseAndCreatedAtBefore(compareDateTime);
        //만료
        matchList.forEach(Match::expire);
        log.info("Scheduling System Automatically Expire Matches :: Affected Matches Count - {}", matchList.size());
        matchRepository.saveAll(matchList);
    }
}
