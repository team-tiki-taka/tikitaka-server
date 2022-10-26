package com.tikitaka.naechinso.domain.point;

import com.tikitaka.naechinso.domain.member.MemberRepository;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.point.constant.PointType;
import com.tikitaka.naechinso.domain.point.dto.PointChargeRequestDTO;
import com.tikitaka.naechinso.domain.point.dto.PointResponseDTO;
import com.tikitaka.naechinso.domain.point.entity.Point;
import com.tikitaka.naechinso.global.error.ErrorCode;
import com.tikitaka.naechinso.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PointService {
    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;

    public PointResponseDTO charge(Member authMember, PointChargeRequestDTO requestDTO) {
        Member member = memberRepository.findByMember(authMember)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Point point = Point.builder()
                .value(requestDTO.getValue())
                .content("포인트 충전")
                .type(PointType.CHARGE)
                .member(member)
                .build();

        pointRepository.save(point);

        return PointResponseDTO.of(point);
    }
}
