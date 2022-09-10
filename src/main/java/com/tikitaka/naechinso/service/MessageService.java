package com.tikitaka.naechinso.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tikitaka.naechinso.dto.MessageDTO;
import com.tikitaka.naechinso.dto.ResponseMessageDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public interface MessageService {
    String makeSignature(Long currentTime);

    ResponseMessageDTO sendMessage(MessageDTO messageDto);
}
