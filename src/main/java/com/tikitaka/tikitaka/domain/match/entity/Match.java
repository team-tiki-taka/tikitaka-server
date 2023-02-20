package com.tikitaka.tikitaka.domain.match.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tikitaka.tikitaka.domain.match.constant.MatchStatus;
import com.tikitaka.tikitaka.domain.member.entity.Member;
import com.tikitaka.tikitaka.global.config.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Match")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"fromMember", "toMember"})
public class Match extends BaseEntity {
    @Id
    @Column(name = "mat_id")
    @GeneratedValue
    private Long id;

    @Column(name = "mat_status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MatchStatus status = MatchStatus.WAIT;

    @Column(name = "mat_is_expired")
    @Builder.Default
    private Boolean isExpired = false;

    /* 호감을 보내는 사람 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_from_id")
    @JsonIgnore
    private Member fromMember;

    /* 호감을 받는 사람 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_to_id")
    @JsonIgnore
    private Member toMember;

    /* 만료 */
    public void expire() {
        this.isExpired = true;
    }
    /* 호감 거절 */
    public void reject() {
        this.status = MatchStatus.REJECTED;
    }
    /* 서로 호감을 보내어 매칭 수락 */
    public void accept() {
        this.status = MatchStatus.ACCEPTED;
    }
    /* 번호 오픈권 사용 */
    public void open() {
        this.status = MatchStatus.OPEN;
    }

}
