package com.example.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.ExponentialBackOff;
import shared.dto.NotificationEvent;


@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NotificationEvent>
    kafkaListenerContainerFactory(
            ConsumerFactory<String, NotificationEvent> consumerFactory) {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, NotificationEvent>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        // Retry с экспоненциальной задержкой. После исчерпания попыток — log и пропуск.
        var backoff = new ExponentialBackOff(1000L, 2.0);
        backoff.setMaxElapsedTime(30_000L);
        factory.setCommonErrorHandler(new DefaultErrorHandler(backoff));

        return factory;
    }
}
