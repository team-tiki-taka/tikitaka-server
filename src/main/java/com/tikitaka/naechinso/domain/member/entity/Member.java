package com.tikitaka.naechinso.domain.member.entity;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.global.config.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

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

    @Column(name = "mem_accpets_service")
    @Builder.Default
    private boolean acceptsService = false;

    @Column(name = "mem_accpets_info")
    @Builder.Default
    private boolean acceptsInfo = false;

    @Column(name = "mem_accpets_religion")
    @Builder.Default
    private boolean acceptsReligion = false;

    @Column(name = "mem_accpets_location")
    @Builder.Default
    private boolean acceptsLocation = false;

    @Column(name = "mem_accpets_marketing")
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




//    //소개해준 사람들
//    @OneToMany
//    @JoinColumn
//    @Builder.Default
//    private List<Member> receivers = new ArrayList<>();
}
