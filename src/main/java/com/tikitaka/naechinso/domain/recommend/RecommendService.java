package com.tikitaka.naechinso.domain.recommend;

import com.tikitaka.naechinso.domain.member.MemberRepository;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.recommend.dto.*;
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
import java.util.UUID;

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

            //자기 자신을 추천하면 종료
            if (senderPhone == dto.getReceiverPhone()) {
                throw new BadRequestException(ErrorCode.CANNOT_RECOMMEND_MYSELF);
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
        //이미 추천사 요청을 보냄
        if (existsByReceiverPhone(receiverPhone)) {
            throw(new NotFoundException(ErrorCode.RECOMMEND_ALREADY_EXIST));
        }

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

    public RecommendDTO updateRecommendRequest(String uuid, String senderPhone, RecommendAcceptRequestDTO dto) {

        Recommend recommend = findByUuid(uuid);

        //자기 자신을 추천하면 종료
        if (senderPhone == recommend.getReceiverPhone()) {
            throw new BadRequestException(ErrorCode.CANNOT_RECOMMEND_MYSELF);
        }

        //유저가 없으면 회원가입 시킴 있으면 그대로 사용
        Member sender = memberRepository.findByPhone(senderPhone)
                .orElse(dto.toSender(senderPhone));

        recommend.setSender(sender);
        recommend.setSenderPhone(senderPhone);
        recommend.setSenderName(dto.getName());
        recommend.setSenderAge(dto.getAge());
        recommend.setSenderGender(dto.getGender());
        recommend.setSenderJobName(dto.getJobName());
        recommend.setSenderJobPart(dto.getJobPart());
        recommend.setSenderJobLocation(dto.getJobLocation());
        recommend.setReceiverName(dto.getReceiverName());
        recommend.setReceiverAge(dto.getReceiverAge());
        recommend.setReceiverAppeal(dto.getAppeal());
        recommend.setReceiverGender(dto.getReceiverGender());
        recommend.setReceiverMeet(dto.getMeet());
        recommend.setReceiverPersonality(dto.getPersonality());

        memberRepository.save(sender);
        recommendRepository.save(recommend);

        return RecommendDTO.of(recommend);
    }


    public List<RecommendDTO> findAll() {
        List<RecommendDTO> recommendDTOList = new ArrayList<>();

        recommendRepository.findAllByIdNotNull().forEach(
                recommend -> recommendDTOList.add(RecommendDTO.of(recommend))
        );

        recommendDTOList.forEach(
                recommendDTO -> System.out.println("recommendDTO = " + recommendDTO)
        );

        return recommendDTOList;
    }

    public List<RecommendDTO> findAllBySenderPhone(String phone) {
        List<RecommendDTO> recommendDTOList = new ArrayList<>();
        recommendRepository.findAllBySenderPhone(phone).stream().map(
                recommend -> recommendDTOList.add(RecommendDTO.of(recommend))
        );
        return recommendDTOList;
    }

    public Recommend findByUuid(String uuid) {
        return recommendRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RECOMMEND_NOT_FOUND));
    }

    public Boolean existsByReceiverPhone(String phone){
        return recommendRepository.existsByReceiverPhone(phone);
    }

}
