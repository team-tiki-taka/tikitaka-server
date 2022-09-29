package com.tikitaka.naechinso.domain.recommend.entity;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.config.entity.BaseEntity;
import com.tikitaka.naechinso.global.config.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

/**
 * 추천사 정보를 담당하는 엔티티입니다
 * @author gengminy 220924
 * */
@Entity
@Table(name = "recommend")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Recommend extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "rec_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_id")
    private Member sender;

    /* 받는 사람이 아직 가입 안했으면 NULL 일수도 있음 */
    @OneToOne
    @JoinColumn(name = "rec_target_id")
    private Member receiver;

    @Column(name = "rec_name")
    private String name;

    @Column(name = "rec_gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "rec_meet")
    private String meet;

    @Column(name = "rec_appeal")
    private String appeal;

    @Column(name = "rec_phone")
    private String phone;
}
