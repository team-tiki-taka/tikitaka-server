package com.tikitaka.tikitaka.infra.sms;

public interface NaverSmsService {
    boolean sendMessage(String to, String message);
}
