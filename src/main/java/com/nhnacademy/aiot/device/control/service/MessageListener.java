package com.nhnacademy.aiot.device.control.service;

import com.nhnacademy.aiot.device.control.adapter.DeviceSettingAdapter;
import com.nhnacademy.aiot.device.control.config.MqttConfig.OutboundGateway;
import com.nhnacademy.aiot.device.control.dto.NotificationRequest;
import com.nhnacademy.aiot.device.control.dto.ValueMessage;
import com.nhnacademy.aiot.device.control.send.MessageSender;
import com.nhnacademy.aiot.device.control.util.CommonUtil;
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

    private static final Long ADMIN_ROLE_ID = 1L;
    private final OutboundGateway outboundGateway;
    private final RedisService redisService;
    private final DeviceSettingAdapter deviceSettingAdapter;
    private final MessageSender messageSender;

    @RabbitListener(queues = AIR_CONDITIONER_QUEUE)
    public void airConditionerProcess(ValueMessage message) {
        outboundGateway.sendToMqtt(message.getValue().toString(), CommonUtil.createDeviceControlTopic(AIR_CONDITIONER, message.getPlace()));
    }

    @RabbitListener(queues = AIR_CLEANER_QUEUE)
    public void airCleanerProcess(ValueMessage message) {
        outboundGateway.sendToMqtt(message.getValue().toString(), CommonUtil.createDeviceControlTopic(AIR_CLEANER, message.getPlace()));
    }

    @RabbitListener(queues = LIGHT_QUEUE)
    public void lightProcess(ValueMessage message) {
        outboundGateway.sendToMqtt(message.getValue().toString(), CommonUtil.createDeviceControlTopic(LIGHT, message.getPlace()));
    }

    @RabbitListener(queues = INTRUSION_QUEUE)
    public void intrusionProcess(ValueMessage message) {
        NotificationRequest notificationRequest = CommonUtil.createIntrusionNotification(ADMIN_ROLE_ID, redisService.getPlaceName(message.getPlace()));
        deviceSettingAdapter.addNotification(notificationRequest);

        String status = message.getValue() ? " 작동했습니다." : " 정지했습니다.";
        messageSender.send("침입감지 봇", redisService.getPlaceName(message.getPlace()) + "의" + "침입감지가" + status);
    }
}