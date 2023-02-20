package com.tikitaka.tikitaka.domain.member.dto;

import com.tikitaka.tikitaka.domain.member.constant.Gender;
import com.tikitaka.tikitaka.domain.member.entity.Member;
import com.tikitaka.tikitaka.global.annotation.Enum;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;

/**
 * 임시회원의 가입을 위한 Dto입니다
 * @author gengminy 220924
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class MemberCommonJoinRequestDTO {
    @ApiModelProperty(example = "닉")
    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @ApiModelProperty(example = "M")
    @Enum(enumClass = Gender.class, message = "성별 입력이 올바르지 않습니다. M 또는 W가 필요합니다")
    private Gender gender;

    @ApiModelProperty(example = "1998")
    @Min(value = 1988, message = "1988-1998 년생까지만 가입 가능합니다")
    @Max(value = 1998, message = "1988-1998 년생까지만 가입 가능합니다")
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

    public static Member toCommonMember(String phone, MemberCommonJoinRequestDTO dto) {
        Member member = Member.builder()
                .phone(phone)
                .name(dto.name)
                .gender(dto.gender)
                .age(dto.age)
                .acceptsService(dto.acceptsService)
                .acceptsInfo(dto.acceptsInfo)
                .acceptsReligion(dto.acceptsReligion)
                .acceptsLocation(dto.acceptsLocation)
                .acceptsMarketing(dto.acceptsMarketing)
                .build();

        return member;
    }
}
