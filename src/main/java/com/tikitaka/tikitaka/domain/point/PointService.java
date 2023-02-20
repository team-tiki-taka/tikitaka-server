package com.tikitaka.tikitaka.domain.point;

import com.tikitaka.tikitaka.domain.member.MemberRepository;
import com.tikitaka.tikitaka.domain.member.entity.Member;
import com.tikitaka.tikitaka.domain.point.constant.PointType;
import com.tikitaka.tikitaka.domain.point.dto.PointChargeRequestDTO;
import com.tikitaka.tikitaka.domain.point.dto.PointHistoryResponseDTO;
import com.tikitaka.tikitaka.domain.point.dto.PointResponseDTO;
import com.tikitaka.tikitaka.domain.point.entity.Point;
import com.tikitaka.tikitaka.global.error.ErrorCode;
import com.tikitaka.tikitaka.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PointService {
    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;

    public PointHistoryResponseDTO getPointHistory(Member authMember) {
        return PointHistoryResponseDTO.of(pointRepository.findAllByMember(authMember));
    }

    public PointResponseDTO charge(Member authMember, PointChargeRequestDTO requestDTO) {
        Member member = memberRepository.findByMember(authMember)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Point point = Point.builder()
                .value(requestDTO.getValue())
                .content("포인트 충전")
                .type(PointType.CHARGE)
                .member(member)
                .build();

        //포인트 충전
        member.chargePoint(requestDTO.getValue());

        memberRepository.save(member);
        pointRepository.save(point);

        return PointResponseDTO.of(member, point);
    }

    /**
     * 포인트를 사용한다
     * @// TODO: 2022/10/27 상품 엔티티를 만들어서 결제하는 것 필요
     * @param authMember
     * @param requestDTO
     * @return
     */
    public PointResponseDTO use(Member authMember, PointChargeRequestDTO requestDTO) {
        Member member = memberRepository.findByMember(authMember)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Point point = Point.builder()
                .value(requestDTO.getValue())
                .content("포인트 사용")
                .type(PointType.USE)
                .member(member)
                .build();

        member.usePoint(requestDTO.getValue());

        memberRepository.save(member);
        pointRepository.save(point);

        return PointResponseDTO.of(member, point);
    }
}
