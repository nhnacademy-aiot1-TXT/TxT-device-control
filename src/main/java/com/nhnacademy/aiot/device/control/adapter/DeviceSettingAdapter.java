package com.nhnacademy.aiot.device.control.adapter;

import com.nhnacademy.aiot.device.control.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "common-api", path = "/api/common")
public interface DeviceSettingAdapter {
    @PostMapping("/notification")
    void addNotification(@RequestBody NotificationRequest notificationRequest);
}
