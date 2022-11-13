package com.tikitaka.naechinso.domain.recommend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendAcceptRequestDTO;
import com.tikitaka.naechinso.domain.recommend.dto.RecommendMemberAcceptRequestDTO;
import com.tikitaka.naechinso.global.config.entity.BaseEntity;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * 추천사 정보를 담당하는 엔티티입니다
 * @author gengminy 220924
 * */
@Entity
@Table(name = "recommend")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Recommend extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "rec_id")
    private Long id;

    @Builder.Default
    private String uuid = UUID.randomUUID().toString();

    /* 추천 해준 사람 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_id")
    @JsonIgnore
    private Member sender;

    @Column(name = "mem_phone")
    private String senderPhone;

    @Column(name = "mem_name")
    private String senderName;

    @Column(name = "mem_gender")
    private Gender senderGender;

    @Column(name = "mem_age")
    private int senderAge;

//    @Column(name = "mem_job_name")
//    private String senderJobName;
//
//    @Column(name = "mem_job_part")
//    private String senderJobPart;
//
//    @Column(name = "mem_job_location")
//    private String senderJobLocation;


//    @Column(name = "mem_edu_name")
//    private String senderEduName;
//
//    @Column(name = "mem_edu_major")
//    private String senderEduPart;
//
//    @Column(name = "mem_edu_level")
//    private String senderEduLevel;


    /* 받는 사람이 아직 가입 안했으면 NULL 일수도 있음 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "rec_target_id")
    private Member receiver;

    @Column(name = "rec_phone")
    private String receiverPhone;

    @Column(name = "rec_name")
    private String receiverName;

    @Column(name = "rec_gender")
    @Enumerated(EnumType.STRING)
    private Gender receiverGender;

    @Column(name = "rec_age")
    private int receiverAge;

    @Column(name = "rec_meet")
    private String receiverMeet;

    @Column(name = "rec_appeals")
    private String receiverAppeals;

    @Column(name = "rec_appeal_detail", length = 500)
    private String receiverAppealDetail;

    @Column(name = "rec_period")
    private String receiverPeriod;

    public void setReceiver(Member receiver) {
        this.receiver = receiver;
    }

    public void update(Member sender, RecommendAcceptRequestDTO requestDTO) {
        this.sender = sender;
        this.senderPhone = sender.getPhone();
        this.senderName = sender.getName();
        this.senderPhone = sender.getPhone();
        this.senderAge = sender.getAge();
        this.senderGender = sender.getGender();
        this.receiverAppeals = StringUtils.join(requestDTO.getAppeals(), ",");
        this.receiverAppealDetail = requestDTO.getAppealDetail();
        this.receiverMeet = requestDTO.getMeet();
        this.receiverPeriod = requestDTO.getPeriod();
    }

    public List<String> getReceiverAppeals() {
        if (this.receiverAppeals != null) {
            return List.of(this.receiverAppeals.split(","));
        }
        return List.of();
    }
}
