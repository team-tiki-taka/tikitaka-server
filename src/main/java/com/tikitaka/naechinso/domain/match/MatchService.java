package com.tikitaka.naechinso.domain.match;

import com.tikitaka.naechinso.domain.card.CardRepository;
import com.tikitaka.naechinso.domain.card.CardService;
import com.tikitaka.naechinso.domain.match.dto.MatchListResponseDTO;
import com.tikitaka.naechinso.domain.match.dto.MatchResponseDTO;
import com.tikitaka.naechinso.domain.match.entity.Match;
import com.tikitaka.naechinso.domain.member.MemberService;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MatchService {
    private final MatchRepository matchRepository;
    private final CardService cardService;

    /**
     * 활성화된 카드에 해당하는 상대에게 호감을 보낸다
     * @param member
     * @return
     */
    public MatchResponseDTO like(Member member) {


        return null;
    }

    public MatchListResponseDTO findAllByMember(Member authMember) {
        return MatchListResponseDTO.of(authMember);
    }

//    /** 모든 매칭 엔티티 인스턴스를 가져온다 */
//    public List<MatchResponseDTO> findAll() {
//        return matchRepository.findAllDTO();
//    }

    /** 호감을 보내는 쪽 사람의 매칭 정보를 모두 가져온다 */
    public List<Match> findAllMatchBySender(Member member) {
        return matchRepository.findAllByFromMember(member);
    }
//
//    /** 호감을 보내는 쪽 사람의 매칭 정보를 모두 가져온다 */
//    public List<MatchResponseDTO> findAllMatchDTOBySender(Member member) {
//        return matchRepository.findAllByFromMember(member);
//    }

    /** 호감을 받는 쪽 사람의 매칭 정보를 모두 가져온다 */
    public List<Match> findAllMatchByToReceiver(Member member) {
        return matchRepository.findAllByToMember(member);
    }
}
