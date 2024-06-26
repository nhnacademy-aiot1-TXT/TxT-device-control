package com.nhnacademy.aiot.device.control.config;

import com.nhn.dooray.client.DoorayHookSender;
import com.nhnacademy.aiot.device.control.send.DoorayMessageSender;
import com.nhnacademy.aiot.device.control.send.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * 장치 제어시 Dooray 메시지를 보내기위한 설정 클래스
 *
 * @author jongsikk
 * @version 1.0.0
 */
@Slf4j
@Configuration
public class DoorayMessageConfig {

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }

    @Bean
    @ConditionalOnClass(DoorayHookSender.class)
    @ConditionalOnProperty(value = "message.dooray.hook-url")
    public DoorayHookSender doorayHookSender(RestTemplate restTemplate, @Value("${message.dooray.hook-url}") String url) {
        return new DoorayHookSender(restTemplate, url);
    }

    @Bean
    @ConditionalOnBean(DoorayHookSender.class)
    public MessageSender messageSender(DoorayHookSender doorayHookSender) {
        return new DoorayMessageSender(doorayHookSender);
    }
}
