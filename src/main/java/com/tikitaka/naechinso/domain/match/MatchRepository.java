package com.tikitaka.naechinso.domain.match;

import com.tikitaka.naechinso.domain.match.constant.MatchStatus;
import com.tikitaka.naechinso.domain.match.dto.MatchResponseDTO;
import com.tikitaka.naechinso.domain.match.entity.Match;
import com.tikitaka.naechinso.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    /* OPEN 이나 ACCEPT 상태가 아닌 것들만 가져와야함 */

    List<Match> findAllByFromMemberAndStatusNotIn(Member member, List<MatchStatus> statusList);

    List<Match> findAllByToMemberAndStatusNotIn(Member member, List<MatchStatus> statusList);

    @Query("SELECT m " +
            "FROM Match m " +
            "JOIN m.toMember t " +
            "WHERE t = :member " +
            "AND m.status <> com.tikitaka.naechinso.domain.match.constant.MatchStatus.ACCEPTED " +
            "AND m.status <> com.tikitaka.naechinso.domain.match.constant.MatchStatus.OPEN")
    List<Match> findAllByToMemberNotComplete(Member member);

    @Query("SELECT m " +
            "FROM Match m " +
            "JOIN m.fromMember f " +
            "WHERE f = :member " +
            "AND m.status <> com.tikitaka.naechinso.domain.match.constant.MatchStatus.ACCEPTED " +
            "AND m.status <> com.tikitaka.naechinso.domain.match.constant.MatchStatus.OPEN")
    List<Match> findAllByFromMemberNotComplete(Member member);

    @Query("SELECT m " +
            "FROM Match m " +
            "JOIN m.fromMember f JOIN m.toMember t " +
            "WHERE (f = :member OR t = :member) " +
            "AND (m.status = com.tikitaka.naechinso.domain.match.constant.MatchStatus.ACCEPTED " +
            "OR m.status = com.tikitaka.naechinso.domain.match.constant.MatchStatus.OPEN)")
    List<Match> findAllByMemberComplete(Member member);

    @Query("SELECT m " +
            "FROM Match m " +
            "JOIN m.fromMember f JOIN m.toMember t " +
            "WHERE m.id = :id AND (f = :member OR t = :member) " +
            "AND m.status = com.tikitaka.naechinso.domain.match.constant.MatchStatus.ACCEPTED")
    Optional<Match> findAllByIdAndMemberAndStatusIsAccept(Long id, Member member);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN 'true' ELSE 'false' END " +
            "FROM Match m " +
            "JOIN m.fromMember f JOIN m.toMember t " +
            "WHERE f.id = :id OR t.id = :id")
    Boolean existsByTargetId(Long id);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN 'true' ELSE 'false' END " +
            "FROM Match m " +
            "JOIN m.fromMember f JOIN m.toMember t " +
            "WHERE (f.id = :id OR t.id = :id) " +
            "AND m.status = com.tikitaka.naechinso.domain.match.constant.MatchStatus.OPEN")
    Boolean existsByTargetIdAndStatusIsOpen(Long id);

    Optional<Match> findByIdAndToMemberAndStatus(Long id, Member member, MatchStatus status);

    Optional<Match> findByIdAndStatus(Long id, MatchStatus status);

//    @Query("select new com.tikitaka.naechinso.domain.match.dto.MatchResponseDTO(mat, m1, m2) " +
//            "from Match mat " +
//            "join mat.toMember m1, mat.fromMember m2")
//    List<MatchResponseDTO> findAllDTO();
}
