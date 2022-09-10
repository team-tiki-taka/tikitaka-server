package com.tikitaka.naechinso.controller;

import com.tikitaka.naechinso.config.CommonApiResponse;
import com.tikitaka.naechinso.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MessageController {
    @GetMapping("/message")
    public CommonApiResponse<String> sendMessage(String content) {
        return CommonApiResponse.of("메세지 전송 요청");
    }

    @GetMapping("/m")
    public String throwError(String content) {
        if (true)
         throw new NotFoundException("not found");
        return content;
    }
}
