package com.nhnacademy.aiot.device.control.util;

import com.nhnacademy.aiot.device.control.dto.NotificationRequest;

import java.time.LocalDateTime;

public class CommonUtil {
    private CommonUtil() {
        throw new IllegalStateException("this class utility class");
    }

    public static NotificationRequest createDeviceControlNotification(Long roleId, String place, String device, boolean value) {
        LocalDateTime time = LocalDateTime.now();
        String contents = place + "에서 " +
                device + "장치가 " +
                (value ? "on" : "off") + " 되었습니다.";

        return new NotificationRequest(time, roleId, contents);
    }

    public static NotificationRequest createIntrusionNotification(Long roleId, String place) {
        LocalDateTime time = LocalDateTime.now();
        String contents = place + "에서 " +
                "재실이 감지되었습니다.";

        return new NotificationRequest(time, roleId, contents);
    }

    public static String createDeviceControlTopic(String device, String place) {
        return "device/".concat(device)
                .concat("/p/")
                .concat(place);
    }
}
