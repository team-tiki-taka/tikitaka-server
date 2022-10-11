package com.tikitaka.naechinso.domain.match.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tikitaka.naechinso.domain.match.constant.MatchStatus;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.config.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "match")
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

}
