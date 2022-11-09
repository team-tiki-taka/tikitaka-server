package com.tikitaka.naechinso.domain.recommend;

import com.tikitaka.naechinso.domain.member.MemberRepository;
import com.tikitaka.naechinso.domain.member.MemberService;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.recommend.dto.*;
import com.tikitaka.naechinso.domain.recommend.entity.Recommend;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.BadRequestException;
import com.tikitaka.naechinso.global.error.exception.InternalServerException;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RecommendService {

    private final RecommendRepository recommendRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    /**
     * 추천사 정보를 가져온다
     * */
    public RecommendListResponseDTO readRecommendList(Member authMember) {
        Member member = memberRepository.findByPhone(authMember.getPhone())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        return RecommendListResponseDTO.of(member);
    }

    /**
     * 휴대폰 번호로 받은 추천사 기본 정보를 가져온다
     * */
    public List<RecommendReceiverDTO> findAllRecommendReceivedListBasicByPhone(String phone) {
        return recommendRepository.findAllByReceiverPhoneAndSenderNotNull(phone)
                .stream().map(RecommendReceiverDTO::of).collect(Collectors.toList());
    }

    /**
     * 임시회원으로 등록하며 가입하지 않은 임의의 유저 추천사를 작성한다
     * */
    public RecommendResponseDTO createRecommend(Member authMember, RecommendBySenderRequestDTO dto) {
        Member sender = memberService.findByMember(authMember);
        String senderPhone = sender.getPhone();
        String receiverPhone = dto.getPhone();

        //자기 자신을 추천하면 종료
        if (senderPhone.equals(receiverPhone)) {
            throw new BadRequestException(ErrorCode.CANNOT_RECOMMEND_MYSELF);
        }

        //동일한 상대를 중복 추천할 수 없음
        if (existsBySenderPhoneAndReceiverPhone(senderPhone, receiverPhone)) {
            throw new BadRequestException(ErrorCode.RECOMMEND_ALREADY_EXIST);
        }

        try {
            //추천 상대가 이미 가입했다면 영속성 엔티티를 가져옴
            Member receiver = memberRepository.findByPhone(dto.getPhone())
                    .orElse(null);

            Recommend recommend = Recommend.builder()
                    .sender(sender)
                    .senderPhone(senderPhone)
                    .senderName(sender.getName())
                    .senderAge(sender.getAge())
                    .senderGender(sender.getGender())
//                    .senderJobName(sender.getJobName())
//                    .senderJobPart(sender.getJobPart())
//                    .senderJobLocation(sender.getJobLocation())
                    .receiver(receiver)
                    .receiverPhone(dto.getPhone())
                    .receiverName(dto.getName())
                    .receiverAge(dto.getAge())
                    .receiverGender(dto.getGender())
                    .receiverAppeals(StringUtils.join(dto.getAppeals(), ","))
                    .receiverAppealDetail(dto.getAppealDetail())
                    .receiverMeet(dto.getMeet())
                    .receiverPeriod(dto.getPeriod())
                    .build();

            recommendRepository.save(recommend);
            return RecommendResponseDTO.of(recommend);
        } catch (Exception e) {
            log.error("추천사 작성 실패");
            throw new InternalServerException(ErrorCode.CANNOT_CREATE_RECOMMEND);
        }
    }

    /**
     * 내 추천사 작성을 요청한다
     * */
    public RecommendResponseDTO createRecommendRequest(Member authMember) {
        //이미 추천사 요청을 보냄
        if (existsByReceiverPhone(authMember.getPhone())) {
            throw(new NotFoundException(ErrorCode.RECOMMEND_REQUEST_ALREADY_EXIST));
        }
        try {
            Member receiver = memberService.findByMember(authMember);
            Recommend recommend = Recommend.builder()
                    .sender(null)
                    .receiver(receiver)
                    .receiverPhone(receiver.getPhone())
                    .receiverName(receiver.getName())
                    .receiverAge(receiver.getAge())
                    .receiverGender(receiver.getGender())
                    .build();

            recommendRepository.save(recommend);

            return RecommendResponseDTO.of(recommend);
        } catch (Exception e) {
            log.error("추천사 작성 요청 생성 실패");
            throw new InternalServerException(ErrorCode.CANNOT_CREATE_RECOMMEND_REQUEST);
        }
    }

    /**
     * 해당 uuid 추천사에 자신을 등록한다
     * */
    public RecommendResponseDTO acceptRecommendByUuid(Member authMember, String uuid, RecommendAcceptRequestDTO dto) {

        Recommend recommend = findByUuidAndReceiverNotNull(uuid);
        Member sender = memberService.findByMember(authMember);

        //이미 작성된 추천사 엔티티면 종료
        if (recommend.getSender() != null) {
            throw new BadRequestException(ErrorCode.RECOMMEND_SENDER_ALREADY_EXIST);
        }

        //자기 자신을 추천하면 종료
        if (sender.getPhone().equals(recommend.getReceiverPhone())) {
            throw new BadRequestException(ErrorCode.CANNOT_RECOMMEND_MYSELF);
        }

        recommend.update(sender, dto);
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

    public Boolean existsBySenderPhoneAndReceiverPhone(String senderPhone, String receiverPhone){
        return recommendRepository.existsBySenderPhoneAndReceiverPhone(senderPhone, receiverPhone);
    }



}
