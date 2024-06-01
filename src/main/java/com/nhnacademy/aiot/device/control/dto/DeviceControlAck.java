package com.nhnacademy.aiot.device.control.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 장치제어후 응답을 받는 DTO 클래스
 *
 * @author jongsikk
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
public class DeviceControlAck {
    private String place;
    private String device;
    private Boolean value;
}
