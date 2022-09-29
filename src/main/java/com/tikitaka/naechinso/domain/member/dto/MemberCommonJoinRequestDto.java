package com.tikitaka.naechinso.domain.member.dto;

import com.tikitaka.naechinso.domain.member.constant.Gender;
import com.tikitaka.naechinso.domain.member.entity.Member;
import com.tikitaka.naechinso.global.annotation.Enum;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;

/**
 * 추천인 및 추천 받는 사람 공통 가입을 위한 Dto입니다
 * @author gengminy 220924
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MemberCommonJoinRequestDto {
    @ApiModelProperty(example = "01010001000")
    @NotBlank(message = "전화번호를 입력해주세요")
    @Pattern(regexp = "[0-9]{10,11}", message = "하이픈 없는 10~11자리 숫자를 입력해주세요")
    private String phone;

    @ApiModelProperty(example = "닉")
    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @ApiModelProperty(example = "M")
    @Enum(enumClass = Gender.class, message = "성별 입력이 올바르지 않습니다. M 또는 W가 필요합니다")
    private Gender gender;

    @ApiModelProperty(example = "25")
    @Min(value = 25, message = "25-33세까지만 가입 가능합니다")
    @Max(value = 33, message = "25-33세까지만 가입 가능합니다")
    private int age;

    @NotNull(message = "서비스 이용약관 동의가 필요합니다")
    @AssertTrue(message = "서비스 이용약관 동의가 필요합니다")
    private boolean acceptsService;

    @NotNull(message = "개인정보 이용 동의가 필요합니다")
    @AssertTrue(message = "개인정보 이용 동의가 필요합니다")
    private boolean acceptsInfo;

    @NotNull(message = "종교 정보 제공 동의가 필요합니다")
    @AssertTrue(message = "종교 정보 제공 동의가 필요합니다")
    private boolean acceptsReligion;

    @NotNull(message = "위치 정보 제공 동의 여부가 필요합니다")
    private boolean acceptsLocation;

    @NotNull(message = "마케팅 동의 여부가 필요합니다")
    private boolean acceptsMarketing;

    @ApiModelProperty(example = "카카오")
    @NotBlank(message = "직장명을 입력해주세요")
    private String jobName;

    @ApiModelProperty(example = "개발자")
    @NotBlank(message = "직장 부서를 입력해주세요")
    private String jobPart;

    @ApiModelProperty(example = "판교")
    @NotBlank(message = "직장 위치를 입력해주세요")
    private String jobLocation;

    public static Member toCommonMember(MemberCommonJoinRequestDto dto) {
        Member member = Member.builder()
                .phone(dto.phone)
                .name(dto.name)
                .gender(dto.gender)
                .age(dto.age)
                .acceptsService(dto.acceptsService)
                .acceptsInfo(dto.acceptsInfo)
                .acceptsReligion(dto.acceptsReligion)
                .acceptsLocation(dto.acceptsLocation)
                .acceptsMarketing(dto.acceptsMarketing)
                .jobName(dto.jobName)
                .jobPart(dto.jobPart)
                .jobLocation(dto.jobLocation)
                .build();

        return member;
    }
}
