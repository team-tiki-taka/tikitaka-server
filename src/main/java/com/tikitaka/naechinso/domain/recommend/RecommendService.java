package com.tikitaka.naechinso.domain.recommend;

import com.tikitaka.naechinso.domain.member.MemberRepository;
import com.tikitaka.naechinso.domain.member.dto.MemberJobUpdateRequestDTO;
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

    /**
     * 추천사 정보를 가져온다
     * */
    public RecommendListResponseDTO readRecommendList(Member authMember) {
        Member member = memberRepository.findByPhone(authMember.getPhone())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        return RecommendListResponseDTO.of(member);
    }

    /**
     * 임시회원으로 등록하며 가입하지 않은 임의의 유저 추천사를 작성한다
     * */
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
                    .receiverPeriod(dto.getPeriod())
                    .build();

            memberRepository.save(sender);
            recommendRepository.save(recommend);

            return RecommendResponseDTO.of(recommend);
    }

    /**
     * 내 추천사 작성을 요청한다
     * */
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

    /**
     * 임시회원으로 가입하며 해당 uuid 추천사에 자신을 등록한다
     * */
    public RecommendResponseDTO updateRecommendRequestWithJoin(String uuid, String senderPhone, RecommendAcceptWithJoinRequestDTO dto) {

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
        Optional<Member> checkSender = memberRepository.findByPhone(senderPhone);
        if(checkSender.isPresent()) {
            throw new BadRequestException(ErrorCode.USER_ALREADY_EXIST);
        }
        Member sender = dto.toSender(senderPhone);

        //유저 정보 업데이트, 가입 정보가 이미 있다면 무시됨, 없으면 생성
        recommend.update(sender, dto);

        memberRepository.save(sender);
        recommendRepository.save(recommend);

        return RecommendResponseDTO.of(recommend);
    }

    /**
     * 추천사 작성 유저가 정회원일 경우 해당 유저 정보로 uuid 에 해당하는 추천사를 등록한다
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

        recommend.update(sender, dto);

        memberRepository.save(sender);
        recommendRepository.save(recommend);

        return RecommendResponseDTO.of(recommend);
    }

    /**
     * 추천사 작성 유저가 임시회원 이상일 경우
     * 해당 유저 정보로 uuid 에 해당하는 추천사를 등록하며
     * 직업 정보를 업데이트 한다
     * */
    public RecommendResponseDTO updateRecommendMemberAccept(String uuid, String senderPhone, RecommendMemberAcceptWithUpdateJobRequestDTO dto) {

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

        MemberJobUpdateRequestDTO updateDTO1 = MemberJobUpdateRequestDTO.builder()
                .jobLocation(dto.getJobLocation())
                .jobPart(dto.getJobPart())
                .jobName(dto.getJobName())
                .build();

        sender.setJob(updateDTO1);

        RecommendMemberAcceptRequestDTO updateDTO2 = RecommendMemberAcceptRequestDTO.builder()
                .appeal(dto.getAppeal())
                .meet(dto.getMeet())
                .period(dto.getPeriod())
                .personality(dto.getPersonality())
                .build();

        recommend.update(sender, updateDTO2);


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
