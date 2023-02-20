package com.tikitaka.tikitaka.domain.recommend;

import com.tikitaka.tikitaka.domain.recommend.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {
    List<Recommend> findAllByReceiver_Id(Long id);
    List<Recommend> findAllByReceiverPhone(String phone);

    List<Recommend> findAllByReceiverPhoneAndSenderNotNull(String phone);
    List<Recommend> findAllBySender_Id(Long id);
    List<Recommend> findAllBySenderPhone(String phone);

    List<Recommend> findAllByIdNotNull();

    Optional<Recommend> findByUuid(String uuid);
    Optional<Recommend> findByUuidAndReceiverNotNull(String uuid);

    Boolean existsByReceiverPhone(String phone);

    Boolean existsByReceiverPhoneAndSenderNotNull(String phone);

    Boolean existsBySenderPhoneAndReceiverPhone(String senderPhone, String receiverPhone);
}
