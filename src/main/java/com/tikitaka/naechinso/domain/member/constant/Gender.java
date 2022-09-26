package com.tikitaka.naechinso.domain.member.constant;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    M,
    W;

    //Enum Validation 을 위한 코드, enum 에 속하지 않으면 null 리턴
    @JsonCreator
    public static Gender fromGender(String val){
        for(Gender gender : Gender.values()){
            if(gender.name().equals(val)){
                return gender;
            }
        }
        return null;
    }
}
