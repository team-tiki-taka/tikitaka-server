package com.tikitaka.tikitaka.domain.point.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PointType {
    CHARGE,
    USE
    ;

    //Enum Validation 을 위한 코드, enum 에 속하지 않으면 null 리턴
    @JsonCreator
    public static PointType fromPointType(String val){
        for(PointType pointType : PointType.values()){
            if(pointType.name().equals(val)){
                return pointType;
            }
        }
        return null;
    }
}
