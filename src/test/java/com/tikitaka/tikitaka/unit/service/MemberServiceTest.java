package com.tikitaka.tikitaka.unit.service;

import com.tikitaka.tikitaka.domain.member.entity.MemberDetail;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @DisplayName("멤버 디테일 문자열 변환 테스트")
    @Test
    void testVerifyCodeBySignedUpMember() {
        String[] str = {"123", "456", "789"};
        MemberDetail memberDetail = MemberDetail.builder()
                .images(Arrays.toString(str))
                .build();

        System.out.println("str = " + Arrays.toString(str));

        System.out.println(List.of(Arrays.toString(str).split(", ")));

        System.out.println("StringUtils.join(str, \",\") = " + StringUtils.join(str, ","));
        System.out.println("StringUtils.join(str, \",\").split(\",\") = " + Arrays.toString(StringUtils.join(str, ",").split(",")));
    }
}
