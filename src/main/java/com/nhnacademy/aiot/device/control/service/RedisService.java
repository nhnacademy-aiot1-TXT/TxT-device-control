package com.nhnacademy.aiot.device.control.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis 관련 유틸리티 클래스
 */
@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, String> redisTemplatePlace;

    public void setDeviceStatus(String key, String hashKey, boolean value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public String getPlaceName(String placeCode) {
        return redisTemplatePlace.opsForValue().get(placeCode);
    }
}