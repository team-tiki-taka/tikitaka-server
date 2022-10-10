package com.tikitaka.naechinso.domain.pending.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tikitaka.naechinso.domain.member.dto.MemberUpdateEduRequestDTO;
import com.tikitaka.naechinso.domain.member.dto.MemberUpdateJobRequestDTO;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.pending.constant.PendingType;
import com.tikitaka.naechinso.global.config.entity.BaseEntity;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "pending")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"member"})
@EqualsAndHashCode
public class Pending extends BaseEntity {

    @Id
    @Column(name = "pen_id")
    @GeneratedValue
    private Long id;

    //가입 대기 중인 타켓
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_id")
    @JsonIgnore
    private Member member;

    @Column(name = "pen_type")
    @Enumerated(EnumType.STRING)
    private PendingType type;

    @Column(name = "pen_content")
    private String content;

    @Column(name = "pen_is_accepted")
    @Builder.Default
    private Boolean isAccepted = false;

    @Column(name = "pen_reason")
    private String reason;

    @Column(name = "pen_images")
    private String images;

    //마지막으로 관리한 어드민 정보
    @Column(name = "pen_admin_id")
    private Long adminId;

    public List<String> getImages() {
        return List.of(this.images.split(","));
    }

    public List<String> updateImage(List<String> images) {
        this.images = StringUtils.join(images, ",");
        return images;
    }

    public void accept(Member adminMember) {
        this.isAccepted = true;
        this.adminId = adminMember.getId();
    }

    public MemberUpdateEduRequestDTO getEduContent() {
        return MemberUpdateEduRequestDTO.of(this.content);
    }

    public MemberUpdateJobRequestDTO getJobContent() {
        return MemberUpdateJobRequestDTO.of(this.content);
    }
}
