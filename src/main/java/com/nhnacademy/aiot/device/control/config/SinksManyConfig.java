package com.nhnacademy.aiot.device.control.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class SinksManyConfig {
    @Bean
    public Sinks.Many<String> sinksMany() {
        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

        sink.tryEmitNext("첫 연결을 원활히 하기 위한 더미 데이터");
        return sink;
    }
}