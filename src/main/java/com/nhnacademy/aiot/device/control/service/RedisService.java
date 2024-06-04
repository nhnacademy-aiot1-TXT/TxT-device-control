package com.nhnacademy.aiot.device.control.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis 관련 유틸리티 클래스
 *
 * @author jongsikk
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, String> redisTemplatePlace;

    /**
     * redis에 device status를 저장하는 메서드
     *
     * @param key
     * @param hashKey
     * @param value
     */
    public void setDeviceStatus(String key, String hashKey, boolean value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public Boolean getDeviceStatus(String key, String hashKey) {
        return (Boolean) redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * place code로 redis에 저장된 place name값을 가져오는 메서드
     *
     * @param placeCode
     * @return place name
     */
    public String getPlaceName(String placeCode) {
        return redisTemplatePlace.opsForValue().get(placeCode);
    }
}