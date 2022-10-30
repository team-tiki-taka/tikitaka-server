package com.tikitaka.naechinso.domain.match.event;

import com.tikitaka.naechinso.domain.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MatchResponseEvent {
    private final Member sender;
    private final Member receiver;
    private final boolean isAccepted;
}
