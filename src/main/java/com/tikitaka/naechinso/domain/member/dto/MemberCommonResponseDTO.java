package com.tikitaka.naechinso.domain.member.dto;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.constant.Role;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.domain.pending.dto.PendingFindResponseDTO;
import com.tikitaka.naechinso.domain.pending.entity.Pending;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 추천인 및 추천 받는 사람 공통 가입을 위한 Dto입니다
 * @author gengminy 220924
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MemberCommonResponseDTO {

    private String phone;

    private Role role;

    private String name;

    private Gender gender;

    private int age;

    private String jobName;

    private String jobPart;

    private String jobLocation;

    private String jobImage;

    private Boolean jobAccepted;

    private String eduName;

    private String eduMajor;

    private String eduLevel;

    private String eduImage;

    private Boolean eduAccepted;

    private long point;

    public static MemberCommonResponseDTO of(Member member) {
        return MemberCommonResponseDTO.builder()
                .phone(member.getPhone())
                .role(member.getRole())
                .name(member.getName())
                .gender(member.getGender())
                .age(member.getAge())
                .jobName(member.getJobName())
                .jobPart(member.getJobPart())
                .jobLocation(member.getJobLocation())
                .jobImage(member.getJobImage())
                .jobAccepted(member.getJobAccepted())
                .eduName(member.getEduName())
                .eduMajor(member.getEduMajor())
                .eduLevel(member.getEduLevel())
                .eduImage(member.getEduImage())
                .eduAccepted(member.getEduAccepted())
                .point(member.getPoint())
                .build();
    }
}
