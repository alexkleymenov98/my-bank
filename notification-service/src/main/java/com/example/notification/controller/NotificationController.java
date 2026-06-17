package com.example.notification.controller;

import com.example.notification.dto.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @PostMapping()
    public Void send(@RequestBody  NotificationRequest request){
        log.info("[NOTIFICATION] login:" + request.login() + "message:" + request.message());

        return null;
    }

}
