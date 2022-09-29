package com.tikitaka.naechinso.domain.recommend;

import com.tikitaka.naechinso.domain.member.MemberRepository;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendListResponseDTO;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendRequestDTO;
import com.tikitaka.naechinso.domain.recommend.entity.Recommend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendService {

    private final RecommendRepository recommendRepository;
    private final MemberRepository memberRepository;

    public RecommendListResponseDTO getRecommendList(Member member) {
        return RecommendListResponseDTO.of(member);
    }

    /**
     * 추천사를 작성한다
     * @param sender 로그인 여부 및 추천인 정보 가져옴
     * @param dto 추천하려는 사람의 정보 dto
     * */
    public RecommendListResponseDTO recommendMember(
            Member sender,
            RecommendRequestDTO dto)
    {
        //작성자가 회원일 경우,
        //추천할 사람이 이미 가입하여 요청했을 경우,
        //둘 모두 회원이 아닐 경우

        Optional<Member> checkMember = memberRepository.findByPhone(dto.getPhone());
        Member receiver;
        //상대가 이미 있는 유저면 FK 로 저장
        if (checkMember.isPresent()) {
            receiver = checkMember.get();
        }
        //상대가 없는 유저면 새로 만든 후 FK 저장
        else {
            receiver = Member.builder()
                    .build();
        }

        Recommend recommend = Recommend.builder()
                .sender(sender)
                .phone(receiver.getPhone())

                .build();
        return RecommendListResponseDTO.of(receiver);
    }
}
