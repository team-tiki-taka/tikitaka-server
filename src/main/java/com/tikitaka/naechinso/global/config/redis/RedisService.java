package com.tikitaka.naechinso.global.config.redis;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public interface RedisService {
    void setValues(String key, String data);

    void setValues(String key, String data, Duration duration);

    String getValues(String key);
    void deleteValues(String key);

    boolean hasKey(String key);
}
