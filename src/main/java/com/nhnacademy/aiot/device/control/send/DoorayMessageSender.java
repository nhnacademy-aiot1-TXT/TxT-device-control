package com.nhnacademy.aiot.device.control.send;

import com.nhn.dooray.client.DoorayHook;
import com.nhn.dooray.client.DoorayHookSender;
import lombok.RequiredArgsConstructor;

/**
 * 장치제어 메시지를 Dooray로 보내기 위한 클래스
 *
 * @author jongsikk
 * @version 1.0.0
 */
@RequiredArgsConstructor
public class DoorayMessageSender implements MessageSender {
    private final DoorayHookSender doorayHookSender;

    @Override
    public void send(String botName, String message) {
        DoorayHook hook = DoorayHook.builder()
                .botName(botName)
                .text(message)
                .build();
        doorayHookSender.send(hook);
    }
}
