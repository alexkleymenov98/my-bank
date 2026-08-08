package shared.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import shared.dto.NotificationEvent;

import java.util.UUID;

@Component
public class NotificationProducer {
    private final KafkaTemplate<String, NotificationEvent> notificationKafkaTemplate;

    @Value("${kafka.topic.notification.name}")
    private String topicNotificationName;

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

        notificationKafkaTemplate.send(topicNotificationName, login, event);
    }
}
