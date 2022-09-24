package com.tikitaka.naechinso.domain.recommend.entity;

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
}
