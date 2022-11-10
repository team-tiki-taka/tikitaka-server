package com.tikitaka.naechinso.domain.member.dto;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.constant.Role;
import com.tikitaka.naechinso.domain.member.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 멤버 전체 검색을 위한 DTO
 * @author gengminy 221008
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MemberFindResponseDTO {

    private Long id;

    private String phone;

    private Role role;

    private String name;

    private Gender gender;

    private int age;

    private String jobName;

    private String jobPart;

    private String jobLocation;

    private String jobImage;

    private String eduName;

    private String eduMajor;

    private String eduLevel;

    private String eduImage;

    private String createdAt;


    public static MemberFindResponseDTO of(Member member) {
        MemberFindResponseDTO res = MemberFindResponseDTO.builder()
                .id(member.getId())
                .phone(member.getPhone())
                .role(member.getRole())
                .name(member.getName())
                .gender(member.getGender())
                .age(member.getAge())
                .jobName(member.getJobName())
                .jobPart(member.getJobPart())
                .jobLocation(member.getJobLocation())
                .jobImage(member.getJobImage())
                .eduName(member.getEduName())
                .eduMajor(member.getEduMajor())
                .eduLevel(member.getEduLevel())
                .eduImage(member.getEduImage())
                .createdAt(member.getCreatedAt().toString())
                .build();

        return res;
    }
}
