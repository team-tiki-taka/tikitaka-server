package com.tikitaka.naechinso.domain.pending;

import com.tikitaka.naechinso.domain.pending.entity.Pending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendingRepository extends JpaRepository<Pending, Long> {
    List<Pending> findAllByMemberId(Long memberId);

    List<Pending> findAllByMemberIdAndIsAcceptedIsTrue(Long memberId);

}
