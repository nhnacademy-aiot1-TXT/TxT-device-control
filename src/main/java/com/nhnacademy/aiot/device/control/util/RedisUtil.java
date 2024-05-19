package com.nhnacademy.aiot.device.control.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis 관련 유틸리티 클래스
 */
@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public void setDeviceStatus(String key, String hashKey, boolean value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }
}