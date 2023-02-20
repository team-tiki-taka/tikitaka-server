package com.tikitaka.tikitaka.infra.image.constant;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * S3 내부의 디렉토리 경로를 담당하는 Enum 입니다
 * @author gengminy 221008
 * */
public enum AwsS3Directory {
    member,
    job,
    edu;

    //Enum Validation 을 위한 코드, enum 에 속하지 않으면 null 리턴
    @JsonCreator
    public static AwsS3Directory fromS3Directory(String val){
        for(AwsS3Directory awsS3Directory : AwsS3Directory.values()){
            if(awsS3Directory.name().equals(val)){
                return awsS3Directory;
            }
        }
        return null;
    }
}
