package com.nhnacademy.aiot.device.control.service;

import com.nhnacademy.aiot.device.control.adapter.DeviceSettingAdapter;
import com.nhnacademy.aiot.device.control.config.MqttConfig.OutboundGateway;
import com.nhnacademy.aiot.device.control.dto.NotificationRequest;
import com.nhnacademy.aiot.device.control.dto.ValueMessage;
import com.nhnacademy.aiot.device.control.send.MessageSender;
import com.nhnacademy.aiot.device.control.util.CommonUtil;
import com.nhnacademy.aiot.device.control.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("prod")
@Service
@RequiredArgsConstructor
public class MessageListener {
    private static final String AIR_CONDITIONER = "airconditioner";
    private static final String AIR_CONDITIONER_QUEUE = "txt.airconditioner.queue";
    private static final String AIR_CLEANER = "aircleaner";
    private static final String AIR_CLEANER_QUEUE = "txt.aircleaner.queue";
    private static final String LIGHT = "light";
    private static final String LIGHT_QUEUE = "txt.light.queue";
    private static final String INTRUSION_QUEUE = "txt.intrusion.queue";
    private static final String DEVICE_KEY = "device_power_status";
    private static final Long ADMIN_ROLE_ID = 1L;
    private static final Long USER_ROLE_ID = 2L;
    private final OutboundGateway outboundGateway;
    private final RedisUtil redisUtil;
    private final DeviceSettingAdapter deviceSettingAdapter;
    private final MessageSender messageSender;

    @RabbitListener(queues = AIR_CONDITIONER_QUEUE)
    public void airConditionerProcess(ValueMessage message) {
        outboundGateway.sendToMqtt(message.getValue().toString(), CommonUtil.createDeviceControlTopic(AIR_CONDITIONER, message.getPlace()));
        redisUtil.setDeviceStatus(DEVICE_KEY, AIR_CONDITIONER.concat(":").concat(message.getPlace()), message.getValue());

        NotificationRequest notificationRequest = CommonUtil.createDeviceControlNotification(USER_ROLE_ID, message.getPlace(), AIR_CONDITIONER, message.getValue());
        deviceSettingAdapter.addNotification(notificationRequest);

        sendMessage("에어컨이", message);
    }

    @RabbitListener(queues = AIR_CLEANER_QUEUE)
    public void airCleanerProcess(ValueMessage message) {
        outboundGateway.sendToMqtt(message.getValue().toString(), CommonUtil.createDeviceControlTopic(AIR_CLEANER, message.getPlace()));
        redisUtil.setDeviceStatus(DEVICE_KEY, AIR_CLEANER.concat(":").concat(message.getPlace()), message.getValue());

        NotificationRequest notificationRequest = CommonUtil.createDeviceControlNotification(USER_ROLE_ID, message.getPlace(), AIR_CLEANER, message.getValue());
        deviceSettingAdapter.addNotification(notificationRequest);

        sendMessage("공기 청정기가", message);
    }

    @RabbitListener(queues = LIGHT_QUEUE)
    public void lightProcess(ValueMessage message) {
        outboundGateway.sendToMqtt(message.getValue().toString(), CommonUtil.createDeviceControlTopic(LIGHT, message.getPlace()));
        redisUtil.setDeviceStatus(DEVICE_KEY, LIGHT.concat(":").concat(message.getPlace()), message.getValue());

        NotificationRequest notificationRequest = CommonUtil.createDeviceControlNotification(USER_ROLE_ID, message.getPlace(), LIGHT, message.getValue());
        deviceSettingAdapter.addNotification(notificationRequest);

        sendMessage("조명이", message);
    }

    @RabbitListener(queues = INTRUSION_QUEUE)
    public void intrusionProcess(ValueMessage message) {
        NotificationRequest notificationRequest = CommonUtil.createIntrusionNotification(ADMIN_ROLE_ID, message.getPlace());
        deviceSettingAdapter.addNotification(notificationRequest);

        String status = message.getValue() ? " 작동했습니다." : " 정지했습니다.";
        messageSender.send("침입감지 봇", message.getPlace() + "의" + "침입감지가" + status);
    }

    private void sendMessage(String device, ValueMessage message) {
        String status = message.getValue() ? " 켜졌습니다." : " 꺼졌습니다.";
        messageSender.send(device.substring(0, device.length() - 1) + " 봇", message.getPlace() + "의" + device + status);
    }
}