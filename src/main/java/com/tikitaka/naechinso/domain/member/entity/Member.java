package com.tikitaka.naechinso.domain.member.entity;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.point.entity.Point;
import com.tikitaka.naechinso.domain.recommend.entity.Recommend;
import com.tikitaka.naechinso.global.config.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 멤버 공통 정보를 담당하는 엔티티입니다
 * @author gengminy 220924
 * */
@Entity
@Table(name = "member")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Member extends BaseEntity {

    @Id
    @Column(name = "mem_id")
    @GeneratedValue
    private Long id;

    @Column(name = "mem_phone")
    private String phone;

    @Column(name = "mem_name")
    private String name;

    @Column(name = "mem_gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "mem_age")
    private int age;

    @Column(name = "mem_accepts_service")
    @Builder.Default
    private boolean acceptsService = false;

    @Column(name = "mem_accepts_info")
    @Builder.Default
    private boolean acceptsInfo = false;

    @Column(name = "mem_accepts_religion")
    @Builder.Default
    private boolean acceptsReligion = false;

    @Column(name = "mem_accepts_location")
    @Builder.Default
    private boolean acceptsLocation = false;

    @Column(name = "mem_accepts_marketing")
    @Builder.Default
    private boolean acceptsMarketing = false;

    @Column(name = "mem_job_name")
    private String jobName;

    @Column(name = "mem_job_part")
    private String jobPart;

    @Column(name = "mem_job_location")
    private String jobLocation;

    //가입했을 경우 정보 디테일
    @OneToOne(mappedBy = "member")
    @JoinColumn(name = "mem_detail")
    private MemberDetail detail;

    //소개해준 사람들 -> recommend 로 넣으면 됨
    //mapped by 에는 연관관계 엔티티의 필드명을 적어줌
    @OneToMany(mappedBy = "sender")
    private List<Recommend> recommends = new ArrayList<>();

    //소개해준 사람들 -> recommend 로 넣으면 됨
    @OneToOne(mappedBy = "receiver")
    private Recommend recommend_received;


    public void setDetail(MemberDetail memberDetail) {
        this.detail = memberDetail;
    }


}
