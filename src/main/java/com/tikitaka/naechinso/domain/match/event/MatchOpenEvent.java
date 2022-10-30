package com.tikitaka.naechinso.domain.match.event;

import com.tikitaka.naechinso.domain.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MatchOpenEvent {
    private final Member opener;
    private final Member opposite;
}
