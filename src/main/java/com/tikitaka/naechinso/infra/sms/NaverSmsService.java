package com.tikitaka.naechinso.infra.sms;

public interface NaverSmsService {
    boolean sendMessage(String to, String message);
}
