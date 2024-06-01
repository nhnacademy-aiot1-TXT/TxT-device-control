package com.nhnacademy.aiot.device.control.send;

/**
 * 장치제어 메시지를 보내기 위한 인터페이스
 *
 * @author jongsikk
 * @version 1.0.0
 */
public interface MessageSender {
    void send(String botName, String message);
}
