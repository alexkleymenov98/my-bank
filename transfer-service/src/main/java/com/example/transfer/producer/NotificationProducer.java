package com.example.transfer.producer;

import com.example.transfer.dto.NotificationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NotificationProducer {
    private final KafkaTemplate<String, NotificationEvent> notificationKafkaTemplate;

    public NotificationProducer(KafkaTemplate<String, NotificationEvent> notificationKafkaTemplate) {
        this.notificationKafkaTemplate = notificationKafkaTemplate;
    }

    public void send(String eventType, String login, String message){
        NotificationEvent event  = new NotificationEvent(
                UUID.randomUUID().toString(),
                eventType,
                login,
                message
        );

        notificationKafkaTemplate.send("notification-topic", login, event);
    }
}
