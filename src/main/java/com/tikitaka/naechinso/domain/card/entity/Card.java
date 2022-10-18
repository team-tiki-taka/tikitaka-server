package com.tikitaka.naechinso.domain.card.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.config.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

/**
 * 멤버 공통 정보를 담당하는 엔티티입니다
 * @author gengminy 220924
 * */
@Entity
@Table(name = "card")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Card extends BaseEntity {
    @Id
    @Column(name = "car_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_id")
    @JsonIgnore
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_target")
    @JsonIgnore
    private Member target;

    @Column(name = "car_target_id")
    private Long targetId;

    @Column(name = "car_is_active")
    @Builder.Default
    private Boolean isActive = false;

    public void disable() {
        this.isActive = false;
    }
}
