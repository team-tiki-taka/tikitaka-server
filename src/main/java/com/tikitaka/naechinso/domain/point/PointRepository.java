package com.tikitaka.naechinso.domain.point;

import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.point.constant.PointType;
import com.tikitaka.naechinso.domain.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {

    List<Point> findAllByMember(Member member);
}
