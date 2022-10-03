package com.tikitaka.naechinso.domain.recommend;

import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.member.entity.MemberDetail;
import com.tikitaka.naechinso.domain.recommend.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecommendRepository extends JpaRepository<Recommend, Long> {
    List<Recommend> findAllByReceiver_Id(Long id);
    List<Recommend> findAllByReceiverPhone(String phone);
    List<Recommend> findAllBySender_Id(Long id);
    List<Recommend> findAllBySenderPhone(String phone);

    List<Recommend> findAllByIdNotNull();

    Optional<Recommend> findByUuid(String uuid);

    Boolean existsByReceiverPhone(String phone);
}
