package com.tikitaka.naechinso.domain.match.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tikitaka.naechinso.domain.match.constant.MatchStatus;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.config.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Match")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"fromMember", "toMember"})
@EqualsAndHashCode
public class Match extends BaseEntity {
    @Id
    @Column(name = "mat_id")
    @GeneratedValue
    private Long id;

    @Column(name = "mat_status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MatchStatus status = MatchStatus.WAIT;

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

    /* 서로 호감을 보내어 매칭 수락 */
    public void accept() {
        this.status = MatchStatus.ACCEPTED;
    }
    /* 번호 오픈권 사용 */
    public void open() {
        this.status = MatchStatus.OPEN;
    }

}
