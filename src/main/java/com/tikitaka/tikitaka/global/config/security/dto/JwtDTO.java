package com.tikitaka.tikitaka.global.config.security.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class JwtDTO {

    //    private Long id;
    private String phoneNumber;
    private String role;

//    public JwtDTO(User user) {
////        this.id = user.getId();
//        this.email = user.getEmail();
//        this.name = user.getName();
//        this.authorities = user.getAuthorities().toString();
//    }

    /* 역할 정보가 없음 (registerToken) */
    public JwtDTO(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        //임시 권한 부여
        this.role = null;
    }

    /* 역할 정보가 있음 (accessToken) */
    public JwtDTO(String phoneNumber, String role) {
        this.phoneNumber = phoneNumber;
        //임시 권한 부여
        this.role = role;
    }
}