package com.tikitaka.naechinso.domain.member.entity;

import com.tikitaka.naechinso.domain.member.dto.MemberDetailJoinRequestDto;
import com.tikitaka.naechinso.domain.point.entity.Point;
import com.tikitaka.naechinso.global.config.entity.BaseEntity;
import com.tikitaka.naechinso.global.config.entity.BaseTimeEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 추천 받은 멤버 가입 정보를 담당하는 엔티티입니다
 * @author gengminy 220924
 * */
@Entity
@Table(name = "member_detail")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"member"})
@EqualsAndHashCode(exclude = {"member"}) //one to one cycle hashcode 방지
public class MemberDetail extends BaseEntity {
    // member_detail_id => mem_id
    @Id
    @Column(name = "mem_id")
    private Long id;

//    //추천해준 사람의 PK
//    @ManyToOne
//    @JoinColumn(name = "mem_id2")
//    private Member recommender;

    @Column(name = "mem_height")
    private int height;

    @Column(name = "mem_address")
    private String address;

    @Column(name = "mem_religion")
    private String religion;

    @Column(name = "mem_drink")
    private String drink;

    @Column(name = "mem_smoke")
    private String smoke;

    @Column(name = "mem_mbti")
    @Builder.Default
    private String mbti = "";

    @Column(name = "mem_personality")
    private String personality;

    @Column(name = "mem_introduce")
    @Builder.Default
    private String introduce = "";

    @Column(name = "mem_hobby")
    @Builder.Default
    private String hobby = "";

    @Column(name = "mem_style")
    @Builder.Default
    private String style = "";

    @Column(name = "mem_picture")
    private String picture;

    @Column(name = "mem_point")
    @Builder.Default
    private Long point = 0L;

    @Column(name = "mem_school")
    private String school;

    @Column(name = "mem_major")
    private String major;

    @Column(name = "mem_edu_level")
    private String eduLevel;

    //    포인트 내역
    @OneToMany(mappedBy = "member")
    private List<Point> points = new ArrayList<>();

    // Member Entity 와 1:1 조인
    // Member PK 그대로 사용
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "mem_id")
    private Member member;


    public static MemberDetail of(MemberDetailJoinRequestDto dto) {
        return MemberDetail.builder()
                .height(dto.getHeight())
                .address(dto.getAddress())
                .religion(dto.getReligion())
                .drink(dto.getDrink())
                .smoke(dto.getSmoke())
                .mbti(dto.getMbti())
                .personality(dto.getPersonality())
                .introduce(dto.getIntroduce())
                .hobby(dto.getHobby())
                .style(dto.getStyle())
                .picture(dto.getPicture())
                .school(dto.getSchool())
                .major(dto.getMajor())
                .eduLevel(dto.getEduLevel())
                .build();
    }

    public static MemberDetail of(Member member, MemberDetailJoinRequestDto dto) {
        return MemberDetail.builder()
                .height(dto.getHeight())
                .address(dto.getAddress())
                .religion(dto.getReligion())
                .drink(dto.getDrink())
                .smoke(dto.getSmoke())
                .mbti(dto.getMbti())
                .personality(dto.getPersonality())
                .introduce(dto.getIntroduce())
                .hobby(dto.getHobby())
                .style(dto.getStyle())
                .picture(dto.getPicture())
                .school(dto.getSchool())
                .major(dto.getMajor())
                .eduLevel(dto.getEduLevel())
                .member(member)
                .build();
    }

}
