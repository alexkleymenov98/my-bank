package com.example.notification.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import shared.dto.NotificationEvent;

@Slf4j
@Component
public class NotificationConsumer {
    @KafkaListener(
            topics = {
                    "notification-topic",
            },
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onEvent(
            @Payload NotificationEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment ack
    ) {
        try {
            log.info("[NOTIFICATION from KAFKA] login:" + event.login() + " type:" +event.eventType() + " message:" + event.message());
            ack.acknowledge();      // commit ТОЛЬКО после успешной обработки
        } catch (Exception ex) {
            log.error("Failed to process event {} from {}, will retry", event.eventId(), topic, ex);
            throw ex;               // DefaultErrorHandler сделает retry/backoff
        }
    }
}

