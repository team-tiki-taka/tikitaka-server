package com.tikitaka.naechinso.domain.member;

import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.member.entity.MemberDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberDetailRepository memberDetailRepository;

    public Long saveMember() {
        Member member = Member.builder()
                .age(25)
                .name("갱미니")
                .phone("01012345678")
                .build();

        memberRepository.save(member);

        return member.getId();
    }

    public Long saveMemberWithDetail() {


        Member recommender = Member.builder()
                .age(25)
                .name("추천인")
                .phone("01012345678")
                .build();

        memberRepository.save(recommender);

        System.out.println("recommender = " + recommender);



        Member member = Member.builder()
                .age(25)
                .name("갱미니")
                .phone("01099999999")
                .build();

        System.out.println("member = " + member);

        memberRepository.save(member);


        MemberDetail memberDetail = MemberDetail.builder()
                .height(185)
                .mbti("INFP")
                .address("서울")
                .member(member)
                .build();

        memberDetailRepository.save(memberDetail);


        System.out.println("memberDetail = " + memberDetail);



        return member.getId();
    }

    public String getMember() {
        return memberRepository.findAll().toString();
    }

}
