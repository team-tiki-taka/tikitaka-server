package com.tikitaka.tikitaka.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tikitaka.tikitaka.domain.member.dto.MemberDetailJoinRequestDTO;
import com.tikitaka.tikitaka.global.config.entity.BaseEntity;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.List;

/**
 * 추천 받은 멤버 가입 정보를 담당하는 엔티티입니다
 * @author gengminy 220924
 * */
@Entity
@Table(name = "Member_detail")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"member"})
public class MemberDetail extends BaseEntity {
    // member_detail_id => mem_id
    @Id
    @Column(name = "mem_id")
    private Long id;

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

    @Column(name = "mem_personalities")
    private String personalities;

    @Column(name = "mem_introduce")
    @Builder.Default
    private String introduce = "";

    @Column(name = "mem_hobby")
    @Builder.Default
    private String hobby = "";

    @Column(name = "mem_style")
    @Builder.Default
    private String style = "";

    @Column(name = "mem_images")
    private String images;

    @Column(name = "mem_image_accepted")
    private boolean image_accepted;

    private boolean deleted;
    public void setDeleted() {
        this.deleted = true;
    }

    // MemberDetail 을 소유한 Member 와 연결
    // Member Entity 와 1:1 조인
    // Member PK 그대로 사용
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "mem_id")
    @JsonIgnore
    private Member member;

    public static MemberDetail of(MemberDetailJoinRequestDTO dto) {
        return MemberDetail.builder()
                .height(dto.getHeight())
                .address(dto.getAddress())
                .religion(dto.getReligion())
                .drink(dto.getDrink())
                .smoke(dto.getSmoke())
                .mbti(dto.getMbti())
                .personalities(StringUtils.join(dto.getPersonalities(), ","))
                .introduce(dto.getIntroduce())
                .hobby(dto.getHobby())
                .style(dto.getStyle())
                .images(StringUtils.join(dto.getImages(), ","))
                .build();
    }

    public static MemberDetail of(Member member, MemberDetailJoinRequestDTO dto) {
        return MemberDetail.builder()
                .height(dto.getHeight())
                .address(dto.getAddress())
                .religion(dto.getReligion())
                .drink(dto.getDrink())
                .smoke(dto.getSmoke())
                .mbti(dto.getMbti())
                .personalities(StringUtils.join(dto.getPersonalities(), ","))
                .introduce(dto.getIntroduce())
                .hobby(dto.getHobby())
                .style(dto.getStyle())
//                .images(StringUtils.join(dto.getImages(), ",")) // image pending
                .member(member)
                .build();
    }

    public List<String> getPersonalities(){
        if (this.personalities != null) {
            return List.of(this.personalities.split(","));
        }
        return List.of();
    }

    public List<String> getImages() {
        if (this.images != null) {
            return List.of(this.images.split(","));
        }
        return List.of();
    }
    public List<String> updateImage(List<String> images) {
        this.images = StringUtils.join(images, ",");
        this.image_accepted = true;
        return images;
    }
}
