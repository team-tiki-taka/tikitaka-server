package com.tikitaka.tikitaka.domain.member.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String detail;

    //Enum Validation 을 위한 코드, enum 에 속하지 않으면 null 리턴
    @JsonCreator
    public static Role fromRole(String val){
        for(Role role : Role.values()){
            if(role.name().equals(val)){
                return role;
            }
        }
        return null;
    }
}
