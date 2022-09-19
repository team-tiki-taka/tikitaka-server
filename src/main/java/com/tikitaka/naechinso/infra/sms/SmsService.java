package com.tikitaka.naechinso.infra.sms;

public interface SmsService {
    boolean sendMessage(String to, String message);
}
