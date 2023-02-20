package com.tikitaka.tikitaka.domain.point.entity;

import com.tikitaka.tikitaka.domain.member.entity.Member;
import com.tikitaka.tikitaka.domain.point.constant.PointType;
import com.tikitaka.tikitaka.global.config.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

/**
 * 포인트 충전 및 사용 정보를 담당하는 엔티티입니다
 * @author gengminy 220924
 * */
@Entity
@Table(name = "point")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Point extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "poi_id")
    private Long id;

    @Column(name = "poi_value")
    private int value;

    @Column(name = "poi_content")
    private String content;

    @Column(name = "poi_type")
    @Enumerated(EnumType.STRING)
    private PointType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_id")
    private Member member;
}
