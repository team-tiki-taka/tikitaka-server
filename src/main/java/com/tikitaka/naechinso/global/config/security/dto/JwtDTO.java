package com.tikitaka.naechinso.global.config.security.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class JwtDTO {

    //    private Long id;
    private String phoneNumber;
    private String authorities;

//    public JwtDTO(User user) {
////        this.id = user.getId();
//        this.email = user.getEmail();
//        this.name = user.getName();
//        this.authorities = user.getAuthorities().toString();
//    }

    public JwtDTO(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        //임시 권한 부여
        this.authorities = "ROLE_USER";
    }
}