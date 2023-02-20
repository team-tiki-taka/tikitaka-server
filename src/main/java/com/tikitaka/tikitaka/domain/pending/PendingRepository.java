package com.tikitaka.tikitaka.domain.pending;

import com.tikitaka.tikitaka.domain.pending.entity.Pending;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PendingRepository extends JpaRepository<Pending, Long> {
    List<Pending> findAllByMemberId(Long memberId);

    List<Pending> findAllByMemberIdAndIsAcceptedIsTrue(Long memberId);

}
