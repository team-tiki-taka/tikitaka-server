package com.tikitaka.naechinso.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tikitaka.naechinso.global.config.entity.BaseEntity;
import com.tikitaka.naechinso.global.config.entity.BaseTimeEntity;
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

    @Column(name = "mem_sex")
    private String sex;

    @Column(name = "mem_age")
    private int age;

    @Column(name = "mem_service_yn")
    @Builder.Default
    private boolean service_yn = false;

    @Column(name = "mem_info_yn")
    @Builder.Default
    private boolean info_yn = false;

    @Column(name = "mem_religion_yn")
    @Builder.Default
    private boolean religion_yn = false;

    @Column(name = "mem_location_yn")
    @Builder.Default
    private boolean location_yn = false;

    @Column(name = "mem_marketing_yn")
    @Builder.Default
    private boolean marketing_yn = false;

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




//    //소개해준 사람들
//    @OneToMany
//    @JoinColumn
//    @Builder.Default
//    private List<Member> receivers = new ArrayList<>();
}
