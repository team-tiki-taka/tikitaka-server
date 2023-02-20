package com.tikitaka.tikitaka.domain;

import com.tikitaka.tikitaka.global.config.CommonApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HealthCheckController {
    @GetMapping
    public CommonApiResponse<Boolean> healthCheck() {
        return CommonApiResponse.of(true);
    }
}
