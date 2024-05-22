package com.nhnacademy.aiot.device.control.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeviceControlAck {
    private String place;
    private String device;
    private Boolean value;
}
