package com.example.cash.client;

import com.example.cash.dto.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class NotificationClient {
    private final RestClient restClient;

    @Value("${services.notification.url}")
    private String notificationUrl;


    public NotificationClient(@Qualifier("customRestClient") RestClient restClient) {
        this.restClient = restClient;
        log.info("BankApiClient initialized with RestClient: {}", restClient.getClass());
    }

    public void send(NotificationRequest request) {
        log.info("send notification" + notificationUrl + "/notification" + request);
        restClient.post()
                .uri(notificationUrl + "/notification")
                .body(request)
                .retrieve()
                .toBodilessEntity();;
    }
}
