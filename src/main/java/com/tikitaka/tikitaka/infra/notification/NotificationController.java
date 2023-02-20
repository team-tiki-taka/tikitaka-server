package com.tikitaka.tikitaka.infra.notification;

import com.tikitaka.tikitaka.domain.member.entity.Member;
import com.tikitaka.tikitaka.global.annotation.AuthMember;
import com.tikitaka.tikitaka.global.config.CommonApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    @ApiOperation(value = "유저 자신의 모든 알림 기록을 (AccessToken)")
    public CommonApiResponse<Object> findAllNotification(
            @ApiIgnore @AuthMember Member member
    ) {
        return CommonApiResponse.of(true);
    }
}
