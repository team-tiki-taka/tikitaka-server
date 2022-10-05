package com.tikitaka.naechinso.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.constant.Role;
import com.tikitaka.naechinso.domain.member.dto.MemberJobUpdateRequestDTO;
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

    @Column(name = "mem_role", columnDefinition = "Text default 'ROLE_USER'")
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

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

    //내가 소개해준 사람들
    //mapped by 에는 연관관계 엔티티의 필드명을 적어줌
    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private List<Recommend> recommends = new ArrayList<>();

    //나를 소개해준 사람들
    @OneToMany(mappedBy = "receiver")
    @JsonIgnore //순환참조 방지, 엔티티 프로퍼티 가려줌
    private List<Recommend> recommend_received = new ArrayList<>();


    public void setDetail(MemberDetail memberDetail) {
        this.detail = memberDetail;
    }

    public void setJob(MemberJobUpdateRequestDTO requestDTO) {
        this.jobName = requestDTO.getJobName();
        this.jobLocation = requestDTO.getJobLocation();
        this.jobPart = requestDTO.getJobPart();
    }
}
