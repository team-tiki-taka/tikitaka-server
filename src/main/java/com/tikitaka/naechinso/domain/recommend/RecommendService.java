package com.tikitaka.naechinso.domain.recommend;

import com.tikitaka.naechinso.domain.member.MemberRepository;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendMemberAcceptRequestDTO;
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

    public RecommendResponseDTO createRecommend(String senderPhone, RecommendResponseDTO dto) {

        return null;
    }

    public RecommendResponseDTO createRecommendJoin(String senderPhone, RecommendJoinRequestDTO dto) {
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

            return RecommendResponseDTO.of(recommend);
    }

    public RecommendResponseDTO createRecommendRequest(String receiverPhone, RecommendRequestDTO dto) {
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

        return RecommendResponseDTO.of(recommend);
    }

    public RecommendResponseDTO updateRecommendRequest(String uuid, String senderPhone, RecommendAcceptRequestDTO dto) {

        Recommend recommend = findByUuidAndReceiverNotNull(uuid);

        //자기 자신을 추천하면 종료
        if (senderPhone.equals(recommend.getReceiverPhone())) {
            throw new BadRequestException(ErrorCode.CANNOT_RECOMMEND_MYSELF);
        }

        //이미 작성된 추천사 엔티티면 종료
        if (recommend.getSender() != null) {
            throw new BadRequestException(ErrorCode.RECOMMEND_SENDER_ALREADY_EXIST);
        }

        //유저가 없으면 회원가입 시킴, 있으면 그대로 사용
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

        return RecommendResponseDTO.of(recommend);
    }

    /**
     * 추천사 작성 유저가 정회원일 경우 유저 정보로 추천사를 등록한다
     *
    * */
    public RecommendResponseDTO updateRecommendMemberAccept(String uuid, String senderPhone, RecommendMemberAcceptRequestDTO dto) {

        Recommend recommend = findByUuidAndReceiverNotNull(uuid);

        //자기 자신을 추천하면 종료
        if (senderPhone.equals(recommend.getReceiverPhone())) {
            throw new BadRequestException(ErrorCode.CANNOT_RECOMMEND_MYSELF);
        }

        //이미 작성된 추천사 엔티티면 종료
        if (recommend.getSender() != null) {
            throw new BadRequestException(ErrorCode.RECOMMEND_SENDER_ALREADY_EXIST);
        }

        //유저가 없으면 400
        Member sender = memberRepository.findByPhone(senderPhone)
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));

        recommend.setSender(sender);
        recommend.setSenderPhone(sender.getPhone());
        recommend.setSenderName(sender.getName());
        recommend.setSenderAge(sender.getAge());
        recommend.setSenderGender(sender.getGender());
        recommend.setSenderJobName(sender.getJobName());
        recommend.setSenderJobPart(sender.getJobPart());
        recommend.setSenderJobLocation(sender.getJobLocation());
        recommend.setReceiverAppeal(dto.getAppeal());
        recommend.setReceiverMeet(dto.getMeet());
        recommend.setReceiverPersonality(dto.getPersonality());

        memberRepository.save(sender);
        recommendRepository.save(recommend);

        return RecommendResponseDTO.of(recommend);
    }

    public List<RecommendResponseDTO> findAll() {
        List<RecommendResponseDTO> recommendResponseDTOList = new ArrayList<>();

        recommendRepository.findAllByIdNotNull().forEach(
                recommend -> recommendResponseDTOList.add(RecommendResponseDTO.of(recommend))
        );

        recommendResponseDTOList.forEach(
                recommendDTO -> System.out.println("recommendDTO = " + recommendDTO)
        );

        return recommendResponseDTOList;
    }

    public List<RecommendResponseDTO> findAllBySenderPhone(String phone) {
        List<RecommendResponseDTO> recommendResponseDTOList = new ArrayList<>();
        recommendRepository.findAllBySenderPhone(phone).stream().map(
                recommend -> recommendResponseDTOList.add(RecommendResponseDTO.of(recommend))
        );
        return recommendResponseDTOList;
    }

    public Recommend findByUuid(String uuid) {
        return recommendRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RECOMMEND_NOT_FOUND));
    }

    public Recommend findByUuidAndReceiverNotNull(String uuid) {
        return recommendRepository.findByUuidAndReceiverNotNull(uuid)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RECOMMEND_RECEIVER_NOT_EXIST));
    }

    public Boolean existsByReceiverPhone(String phone){
        return recommendRepository.existsByReceiverPhone(phone);
    }

    public Boolean existsByReceiverPhoneAndSenderNotNull(String phone){
        return recommendRepository.existsByReceiverPhoneAndSenderNotNull(phone);
    }

}
