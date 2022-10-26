package com.tikitaka.naechinso.domain.match;

import com.tikitaka.naechinso.domain.match.dto.MatchResponseDTO;
import com.tikitaka.naechinso.domain.match.entity.Match;
import com.tikitaka.naechinso.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findAllByFromMember(Member member);
    List<Match> findAllByToMember(Member member);


//    @Query("select new com.tikitaka.naechinso.domain.match.dto.MatchResponseDTO(mat, m1, m2) " +
//            "from Match mat " +
//            "join mat.toMember m1, mat.fromMember m2")
//    List<MatchResponseDTO> findAllDTO();
}
