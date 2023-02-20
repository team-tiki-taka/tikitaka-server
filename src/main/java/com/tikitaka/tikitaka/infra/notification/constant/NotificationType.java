package com.tikitaka.tikitaka.infra.notification.constant;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum NotificationType {
    ALARM,

    //호감 및 매칭 알람
    MATCH,

    //마케팅 메세지
    MARKETING

    ;


    //Enum Validation 을 위한 코드, enum 에 속하지 않으면 null 리턴
    @JsonCreator
    public static NotificationType fromNotificationType(String val){
        for(NotificationType notificationType : NotificationType.values()){
            if(notificationType.name().equals(val)){
                return notificationType;
            }
        }
        return null;
    }
}
