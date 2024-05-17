package com.nhnacademy.aiot.device.control.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RestController
@RequestMapping("/api/device/control/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final Sinks.Many<String> sink;

    @GetMapping(path = "/battery", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> sendBatteryNotification() {
        return sink.asFlux()
                .map(e -> ServerSentEvent.<String>builder()
                        .data(e)
                        .event("기기 배터리 잔량 알림")
                        .build());
    }
}