package com.tikitaka.naechinso.domain.match.constant;

public enum MatchStatus {
    EXPIRED,    //기간(7일) 만료
    WAIT,       //호감 보내기 전 상태
    PENDING,    //호감을 보낸 상태
    ACCEPTED,   //상대가 승낙
    REJECTED    //상대가 거절
}
