package com.tikitaka.naechinso.domain.member;

import com.tikitaka.naechinso.domain.member.entity.MemberDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDetailRepository extends JpaRepository<MemberDetail, Long> {
}
