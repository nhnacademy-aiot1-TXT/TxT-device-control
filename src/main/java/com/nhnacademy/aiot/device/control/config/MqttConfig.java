package com.nhnacademy.aiot.device.control.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.aiot.device.control.adapter.DeviceSettingAdapter;
import com.nhnacademy.aiot.device.control.dto.DeviceControlAck;
import com.nhnacademy.aiot.device.control.dto.NotificationRequest;
import com.nhnacademy.aiot.device.control.send.MessageSender;
import com.nhnacademy.aiot.device.control.service.RedisService;
import com.nhnacademy.aiot.device.control.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;

import java.util.Objects;

/**
 * Mqtt Client 설정 클래스
 *
 * @author jongsikk
 * @version 1.0.0
 */
@Configuration
@RequiredArgsConstructor
@IntegrationComponentScan
public class MqttConfig {
    private static final String BROKER_URL = "tcp://133.186.153.19:1883";
    private static final String MQTT_CLIENT_ID = MqttAsyncClient.generateClientId();
    private static final String TOPIC_FILTER = "txt";
    private static final String DEVICE_KEY = "device_power_status";
    private static final Long USER_ROLE_ID = 2L;
    private final RedisService redisService;
    private final DeviceSettingAdapter deviceSettingAdapter;
    private final MessageSender messageSender;

    private MqttConnectOptions connectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setServerURIs(new String[]{"tcp://133.186.153.19:1883"});
        return options;
    }

    @Bean
    public DefaultMqttPahoClientFactory defaultMqttPahoClientFactory() {
        DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
        clientFactory.setConnectionOptions(connectOptions());
        return clientFactory;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutBound(DefaultMqttPahoClientFactory clientFactory) {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler("txt", clientFactory);

        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(1);

        return messageHandler;
    }

    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    public interface OutboundGateway {
        void sendToMqtt(String payload, @Header(MqttHeaders.TOPIC) String topic);
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inboundChannel() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(BROKER_URL, MQTT_CLIENT_ID, TOPIC_FILTER);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler inboundMessageHandler() {
        return message -> {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                DeviceControlAck deviceControlAck = objectMapper.readValue((String) message.getPayload(), DeviceControlAck.class);
                Boolean currentDeviceStatus = redisService.getDeviceStatus(DEVICE_KEY, deviceControlAck.getPlace().concat("_").concat(deviceControlAck.getDevice()));
                if (Objects.isNull(currentDeviceStatus)) {
                    redisService.setDeviceStatus(DEVICE_KEY, deviceControlAck.getPlace().concat("_").concat(deviceControlAck.getDevice()), deviceControlAck.getValue());
                } else if (!deviceControlAck.getValue().equals(currentDeviceStatus)) {
                    redisService.setDeviceStatus(DEVICE_KEY, deviceControlAck.getPlace().concat("_").concat(deviceControlAck.getDevice()), deviceControlAck.getValue());

                    NotificationRequest notificationRequest = CommonUtil.createDeviceControlNotification(USER_ROLE_ID, redisService.getPlaceName(deviceControlAck.getPlace()), deviceControlAck.getDevice(), deviceControlAck.getValue());
                    deviceSettingAdapter.addNotification(notificationRequest);

                    sendMessage(deviceControlAck.getDevice() + "이", deviceControlAck);
                } else {
                    NotificationRequest notificationRequest = CommonUtil.createNotDeviceControlNotification(USER_ROLE_ID, redisService.getPlaceName(deviceControlAck.getPlace()), deviceControlAck.getDevice());
                    deviceSettingAdapter.addNotification(notificationRequest);

                    messageSender.send("장치 제어 봇", redisService.getPlaceName(deviceControlAck.getPlace()) + "의 " + deviceControlAck.getDevice() + "를 제어하지 못했습니다.");
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private void sendMessage(String device, DeviceControlAck deviceControlAck) {
        String status = Boolean.TRUE.equals(deviceControlAck.getValue()) ? " 켜졌습니다." : " 꺼졌습니다.";
        messageSender.send("장치 제어 봇", redisService.getPlaceName(deviceControlAck.getPlace()) + "의 " + device + status);
    }
}
