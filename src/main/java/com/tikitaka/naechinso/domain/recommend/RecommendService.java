package com.tikitaka.naechinso.domain.recommend;

import com.tikitaka.naechinso.domain.member.MemberRepository;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendDTO;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendJoinRequestDTO;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendListResponseDTO;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendRequestDTO;
import com.tikitaka.naechinso.domain.recommend.entity.Recommend;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.BadRequestException;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendService {

    private final RecommendRepository recommendRepository;
    private final MemberRepository memberRepository;

    public RecommendListResponseDTO readRecommendList(Member authMember) {
        Member member = memberRepository.findByPhone(authMember.getPhone())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        return RecommendListResponseDTO.of(member);
    }

    public RecommendDTO createRecommendJoin(String senderPhone, RecommendJoinRequestDTO dto) {
            //멤버가 이미 있으면 종료
            Optional<Member> checkSender = memberRepository.findByPhone(senderPhone);
            if (checkSender.isPresent()) {
                throw new BadRequestException(ErrorCode.USER_ALREADY_EXIST);
            }

            Member sender = dto.toSender(senderPhone);

            Member receiver = memberRepository.findByPhone(dto.getReceiverPhone())
                    .orElse(null);

            Recommend recommend = Recommend.builder()
                    .sender(sender)
                    .senderPhone(senderPhone)
                    .senderName(dto.getName())
                    .senderAge(dto.getAge())
                    .senderGender(dto.getGender())
                    .senderJobName(dto.getJobName())
                    .senderJobPart(dto.getJobPart())
                    .senderJobLocation(dto.getJobLocation())
                    .receiver(receiver)
                    .receiverPhone(dto.getReceiverPhone())
                    .receiverName(dto.getReceiverName())
                    .receiverAge(dto.getReceiverAge())
                    .receiverAppeal(dto.getAppeal())
                    .receiverGender(dto.getReceiverGender())
                    .receiverMeet(dto.getMeet())
                    .receiverPersonality(dto.getPersonality())
                    .build();

            memberRepository.save(sender);
            recommendRepository.save(recommend);

            return RecommendDTO.of(recommend);
    }

    public RecommendDTO createRecommendRequest(String receiverPhone, RecommendRequestDTO dto) {
        Member receiver = dto.toReceiver(receiverPhone);
        Recommend recommend = Recommend.builder()
                .sender(null)
                .receiver(receiver)
                .receiverPhone(receiverPhone)
                .receiverName(dto.getName())
                .receiverAge(dto.getAge())
                .receiverGender(dto.getGender())
                .build();

        memberRepository.save(receiver);
        recommendRepository.save(recommend);

        return RecommendDTO.of(recommend);
    }

    public List<RecommendDTO> findAllBySenderPhone(String phone) {
        List<RecommendDTO> recommendDTOList = new ArrayList<>();
        recommendRepository.findAllBySenderPhone(phone).stream().map(
                recommend -> recommendDTOList.add(RecommendDTO.of(recommend))
        );
        return recommendDTOList;
    }

//    //링크로 요청한
//    public RecommendDTO findAllRecommendRequestsByUuid(String uuid){
//        RecommendMeta meta = recommendMetaRepository.findByUuid(uuid)
//                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
//        return RecommendDTO.of(meta.getRecommend());
//    }

    public Boolean existsByReceiverPhone(String phone){
        return recommendRepository.existsByReceiverPhone(phone);
    }

}
