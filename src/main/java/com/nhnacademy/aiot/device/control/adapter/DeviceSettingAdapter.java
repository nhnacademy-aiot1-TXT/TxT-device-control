package com.nhnacademy.aiot.device.control.adapter;

import com.nhnacademy.aiot.device.control.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 장치 제어서버에서 사용하는 Feign 요청 클래스
 *
 * @author jongsikk
 * @version 1.0.0
 */
@FeignClient(value = "common-api", path = "/api/common")
public interface DeviceSettingAdapter {
    @PostMapping("/notification")
    void addNotification(@RequestBody NotificationRequest notificationRequest);
}
