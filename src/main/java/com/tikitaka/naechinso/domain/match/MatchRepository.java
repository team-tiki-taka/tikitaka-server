package com.tikitaka.naechinso.domain.match;

import com.tikitaka.naechinso.domain.match.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
}
