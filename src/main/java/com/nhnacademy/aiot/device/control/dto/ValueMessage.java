package com.nhnacademy.aiot.device.control.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 프론트 서버에서의 장치제어 요청값 DTO 클래스
 *
 * @author jongsikk
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValueMessage {
    private String place;
    private Boolean value;
}