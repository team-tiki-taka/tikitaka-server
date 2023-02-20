package com.tikitaka.tikitaka.domain.pending.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tikitaka.tikitaka.domain.member.dto.MemberUpdateEduRequestDTO;
import com.tikitaka.tikitaka.domain.member.dto.MemberUpdateJobRequestDTO;
import com.tikitaka.tikitaka.domain.member.entity.Member;
import com.tikitaka.tikitaka.domain.pending.constant.PendingType;
import com.tikitaka.tikitaka.global.config.entity.BaseEntity;
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

    @Column(name = "pen_images")
    private String images;
    @Column(name = "pen_reason")
    private String reason;

    @Column(name = "pen_reject_images")
    private String rejectImages;

    //마지막으로 관리한 어드민 정보
    @Column(name = "pen_admin_id")
    private Long adminId;

    public List<String> getImages() {
        if (this.images != null) {
            return List.of(this.images.split(","));
        }
        return List.of();
    }

    public List<String> getRejectImages() {
        if (this.rejectImages != null) {
            return List.of(this.rejectImages.split(","));
        }
        return List.of();
    }

    public List<String> updateImage(List<String> images) {
        this.images = StringUtils.join(images, ",");
        return images;
    }

    public void accept(Member adminMember) {
        this.isAccepted = true;
        this.adminId = adminMember.getId();
    }

    public void reject(Member adminMember, String reason, List<String> rejectImages) {
        this.isAccepted = false;
        this.reason = reason;
        this.rejectImages = StringUtils.join(rejectImages, ",");
        this.adminId = adminMember.getId();
    }

    public MemberUpdateEduRequestDTO getEduContent() {
        return MemberUpdateEduRequestDTO.of(this.content);
    }

    public MemberUpdateJobRequestDTO getJobContent() {
        return MemberUpdateJobRequestDTO.of(this.content);
    }
}
