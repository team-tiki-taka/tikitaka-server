package com.tikitaka.tikitaka.domain.member;

import com.tikitaka.tikitaka.domain.member.entity.MemberDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberDetailRepository extends JpaRepository<MemberDetail, Long> {
}
