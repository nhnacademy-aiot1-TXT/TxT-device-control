package com.nhnacademy.aiot.device.control.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 장치제어후 알림을 저장하기위한 DTO 클래스
 *
 * @author jongsikk
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private LocalDateTime time;
    private Long roleId;
    private String contents;
}
