package com.tikitaka.naechinso.domain.match;

import com.tikitaka.naechinso.domain.match.entity.Match;
import com.tikitaka.naechinso.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findAllByFromMember(Member member);
    List<Match> findAllByToMember(Member member);
}
