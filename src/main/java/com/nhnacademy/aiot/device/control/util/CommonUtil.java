package com.nhnacademy.aiot.device.control.util;

import com.nhnacademy.aiot.device.control.dto.NotificationRequest;

import java.time.LocalDateTime;

/**
 * 서버에서 사용할 수 있는 Utility 클래스
 *
 * @author jongsikk
 * @version 1.0.0
 */
public class CommonUtil {
    private CommonUtil() {
        throw new IllegalStateException("this class utility class");
    }

    /**
     * 장치 제어후 알림 전송을 위한 DTO를 생성하는 메서드
     *
     * @param roleId
     * @param place
     * @param device
     * @param value
     * @return notification request
     */
    public static NotificationRequest createDeviceControlNotification(Long roleId, String place, String device, boolean value) {
        LocalDateTime time = LocalDateTime.now();
        String contents = place + "에서 " +
                device + "장치가 " +
                (value ? "on" : "off") + " 되었습니다.";

        return new NotificationRequest(time, roleId, contents);
    }

    /**
     * 침입 감지시 알림 전송을 위한 DTO를 생성하는 클래스
     *
     * @param roleId
     * @param place
     * @return notification request
     */
    public static NotificationRequest createIntrusionNotification(Long roleId, String place) {
        LocalDateTime time = LocalDateTime.now();
        String contents = place + "에서 " +
                "재실이 감지되었습니다.";

        return new NotificationRequest(time, roleId, contents);
    }

    /**
     * 제어 신호를 mqtt로 보내기위한 토픽을 만드는 메서드
     *
     * @param device
     * @param place
     * @return mqtt topic string
     */
    public static String createDeviceControlTopic(String device, String place) {
        return "device/".concat(device)
                .concat("/p/")
                .concat(place);
    }
}
