package com.tikitaka.tikitaka.domain.match.event;

import com.tikitaka.tikitaka.domain.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MatchOpenEvent {
    private final Member opener;
    private final Member opposite;
}
