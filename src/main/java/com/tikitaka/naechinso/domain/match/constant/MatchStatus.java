package com.tikitaka.naechinso.domain.match.constant;

public enum MatchStatus {
    WAIT,       //호감 보내기 전 상태
    PENDING,    //호감을 보낸 상태
    ACCEPTED,   //상대가 승낙
    REJECTED,    //상대가 거절
    OPEN        //번호를 오픈함
}
