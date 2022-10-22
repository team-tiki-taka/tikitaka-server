package com.tikitaka.naechinso.domain.member;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByPhone(String phone);

//    Optional<Member> findTopByIdNotInAndGenderNotAndDetailNotNull(Collection<Long> ids, Gender gender);
    List<Member> findByIdNotInAndGenderNotAndDetailNotNull(Collection<Long> ids, Gender gender);

    @Query("SELECT m FROM Member m WHERE m = :member")
    Optional<Member> findByMember(Member member);


    Optional<Member> findTopByGenderNot(Gender gender);

}
