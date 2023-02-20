package com.tikitaka.tikitaka.domain.pending.constant;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PendingType {
    MEMBER,
    JOB,
    EDU;

    //Enum Validation 을 위한 코드, enum 에 속하지 않으면 null 리턴
    @JsonCreator
    public static PendingType fromPendingType(String val){
        for(PendingType pendingType : PendingType.values()){
            if(pendingType.name().equals(val)){
                return pendingType;
            }
        }
        return null;
    }
}
