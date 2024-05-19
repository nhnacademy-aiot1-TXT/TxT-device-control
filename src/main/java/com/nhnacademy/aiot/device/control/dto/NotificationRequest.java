package com.nhnacademy.aiot.device.control.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private LocalDateTime time;
    private Long roleId;
    private String contents;
}
