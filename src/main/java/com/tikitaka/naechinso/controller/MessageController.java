package com.tikitaka.naechinso.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tikitaka.naechinso.config.CommonApiResponse;
import com.tikitaka.naechinso.dto.MessageDTO;
import com.tikitaka.naechinso.dto.ResponseMessageDTO;
import com.tikitaka.naechinso.exception.NotFoundException;
import com.tikitaka.naechinso.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/send")
    public CommonApiResponse<ResponseMessageDTO> sendMessage(String content) {
        content = "인증번호는 1234 입니다";
        MessageDTO messageDTO = new MessageDTO("01028883492", content);
        ResponseMessageDTO responseMessageDTO = messageService.sendMessage(messageDTO);
        return CommonApiResponse.of(responseMessageDTO);
    }

    @GetMapping("/m")
    public String throwError(String content) {
        if (true)
         throw new NotFoundException("not found");
        return content;
    }
}
