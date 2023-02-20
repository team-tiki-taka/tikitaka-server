package com.tikitaka.tikitaka.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tikitaka.tikitaka.domain.card.entity.Card;
import com.tikitaka.tikitaka.domain.match.entity.Match;
import com.tikitaka.tikitaka.domain.member.constant.Gender;
import com.tikitaka.tikitaka.domain.member.constant.Role;
import com.tikitaka.tikitaka.domain.member.dto.*;
import com.tikitaka.tikitaka.domain.pending.entity.Pending;
import com.tikitaka.tikitaka.domain.point.constant.PointType;
import com.tikitaka.tikitaka.domain.point.entity.Point;
import com.tikitaka.tikitaka.domain.recommend.entity.Recommend;
import com.tikitaka.tikitaka.global.config.entity.BaseEntity;
import com.tikitaka.tikitaka.global.error.ErrorCode;
import com.tikitaka.tikitaka.global.error.exception.BadRequestException;
import com.tikitaka.tikitaka.global.error.exception.UnauthorizedException;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 멤버 공통 정보를 담당하는 엔티티입니다
 * @author gengminy 220924
 * */
@Entity
@Table(name = "Member")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@SQLDelete(sql = "UPDATE member SET deleted = true WHERE mem_id = ?")
@Where(clause = "deleted = false")
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

    @Column(name = "mem_accepts_push")
    @Builder.Default
    private boolean acceptsPush = true;

    @Column(name = "mem_job_name")
    private String jobName;

    @Column(name = "mem_job_part")
    private String jobPart;

    @Column(name = "mem_job_location")
    private String jobLocation;

    @Column(name = "mem_job_image")
    private String jobImage;

    @Column(name = "mem_job_accepted")
    @Builder.Default
    private Boolean jobAccepted = false;

    @Column(name = "mem_edu_school")
    private String eduName;

    @Column(name = "mem_edu_major")
    private String eduMajor;

    @Column(name = "mem_edu_level")
    private String eduLevel;

    @Column(name = "mem_edu_image")
    private String eduImage;

    @Column(name = "mem_edu_accepted")
    @Builder.Default
    private Boolean eduAccepted = false;

    @Column(name = "mem_join_accepted")
    @Builder.Default
    private Boolean joinAccepted = false;

    @Column(name = "mem_point")
    @Builder.Default
    private Long point = 0L;

    @Column(name = "mem_fcm_token")
    @Builder.Default
    private String fcmToken = "";

    private boolean deleted = Boolean.FALSE;

    /**
     * soft delete 처리 로직
     */
    public void setDeleted(){
        this.deleted = true;
    }

    //멤버 디테일 정보
    @OneToOne(mappedBy = "member")
    @JoinColumn(name = "mem_detail")
    private MemberDetail detail;

    //내 가입 대기 정보
    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    @JsonIgnore
    @Builder.Default
    private List<Pending> pending = new ArrayList<>();

    //포인트 내역
    @OneToMany(mappedBy = "member")
    @ToString.Exclude
    @JsonIgnore
    @Builder.Default
    private List<Point> points = new ArrayList<>();

//    @OneToMany(mappedBy = "member")
//    @ToString.Exclude
//    @JsonIgnore
//    private List<Notification> notifications = new ArrayList<>();

    //내가 보낸 호감 내역
    @OneToMany(mappedBy = "fromMember")
    @ToString.Exclude
    @JsonIgnore
    @Builder.Default
    private List<Match> matchesTo = new ArrayList<>();

    //내가 받은 호감 내역
    @OneToMany(mappedBy = "toMember")
    @ToString.Exclude
    @JsonIgnore
    @Builder.Default
    private List<Match> matchesFrom = new ArrayList<>();

    //내가 소개해준 사람들
    //mapped by 에는 연관관계 엔티티의 필드명을 적어줌
    @OneToMany(mappedBy = "sender")
    @ToString.Exclude
    @JsonIgnore
    @Builder.Default
    private List<Recommend> recommends = new ArrayList<>();

    //나를 소개해준 사람들
    @OneToMany(mappedBy = "receiver")
    @ToString.Exclude
    @JsonIgnore //순환참조 방지, 엔티티 프로퍼티 가려줌
    @Builder.Default
    private List<Recommend> recommendReceived = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    @Builder.Default
    private List<Card> cards = new ArrayList<>();


    public void addRecommend(Recommend recommend) {
        this.recommends.add(recommend);
    }

    public void addRecommendReceived(Recommend recommend) {
        this.recommendReceived.add(recommend);
    }

    public void setDetail(MemberDetail memberDetail) {
        this.detail = memberDetail;
    }

    public void updateCommonInfo(MemberDetailJoinRequestDTO requestDTO) {
        this.name = requestDTO.getName();
        this.age = requestDTO.getAge();
        this.gender = requestDTO.getGender();
    }

    public void updateAccepts(MemberUpdateAcceptsRequestDTO requestDTO) {
        this.acceptsMarketing = requestDTO.isAcceptsMarketing();
        this.acceptsPush = requestDTO.isAcceptsPush();
        this.acceptsLocation = requestDTO.isAcceptsLocation();
    }

    public void updateJob(MemberUpdateJobRequestDTO requestDTO) {
        this.jobName = requestDTO.getJobName();
        this.jobLocation = requestDTO.getJobLocation();
        this.jobPart = requestDTO.getJobPart();
        this.jobImage = requestDTO.getJobImage();

        this.jobAccepted = true;
    }

    public void updateEdu(MemberUpdateEduRequestDTO requestDTO) {
        this.eduName = requestDTO.getEduName();
        this.eduMajor = requestDTO.getEduMajor();
        this.eduLevel = requestDTO.getEduLevel();
        this.eduImage = requestDTO.getEduImage();

        this.eduAccepted = true;
    }

    public void updateCommon(MemberUpdateCommonRequestDTO dto) {
        this.name = dto.getName();
        this.gender = dto.getGender();
        this.age = dto.getAge();
    }

    public List<String> updateImage(List<String> images) {
        if (this.detail == null) {
            throw new UnauthorizedException(ErrorCode._UNAUTHORIZED);
        }
        return this.detail.updateImage(images);
    }

    /** 포인트를 충전합니다 */
    public void chargePoint(long value) {
        this.point += value;
    }

    /** 포인트를 사용합니다 */
    public void usePoint(long value) {
        if (this.point - value >= 0) {
            this.point -= value;
        } else {
            throw new BadRequestException(ErrorCode.POINT_NOT_ENOUGH);
        }
    }
    
    public boolean validatePoint() {
        long sum = 0;
        for (Point pointHistory : this.points) {
            if (pointHistory.getType() == PointType.CHARGE) {
                sum += pointHistory.getValue();
            } else if (pointHistory.getType() == PointType.USE){
                sum -= pointHistory.getValue();
            }
        }

        if (sum != point) {
            System.out.println("잔여 포인트 검증 오류");
            return false;
        }
        return true;
    }

    public void acceptJobImage() {
        this.jobAccepted = true;
    }

    public void acceptEduImage() {
        this.eduAccepted = true;
    }

    public void setFcmToken(String token) { this.fcmToken = token; }
    public void deleteFcmToken() { this.fcmToken = ""; }



    /**
     * 테스트 이후 삭제합니다
     * 테스트 이후 삭제합니다
     * 테스트 이후 삭제합니다
     * 테스트 이후 삭제합니다
     * 테스트 이후 삭제합니다
     * 테스트 이후 삭제합니다
     * 테스트 이후 삭제합니다
     * */
    public void setAdmin() {
        this.role = Role.ADMIN;
    }

}
