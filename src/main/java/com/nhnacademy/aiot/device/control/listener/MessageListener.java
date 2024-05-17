package com.nhnacademy.aiot.device.control.listener;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class MessageListener {
    private final Sinks.Many<String> sink;

    @RabbitListener(queues = {"txt.battery.queue"})
    public void receiveBatteryMessage(final Message msg) {
        String payload = new String(msg.getBody(), StandardCharsets.UTF_8);

        JSONObject jsonObject = new JSONObject(payload);
        int batteryLevel = jsonObject.getInt("value");
        String place = jsonObject.getString("place");
        String device = jsonObject.getString("device");
        String event = place + "에 위치한 " + device  + "의 배터리 잔량 : " + batteryLevel;

        System.out.println(event);
        sink.tryEmitNext(event);
    }
}