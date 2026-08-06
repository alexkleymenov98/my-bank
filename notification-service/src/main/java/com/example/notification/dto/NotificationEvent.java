package com.example.notification.dto;


public record NotificationEvent(
        String eventId,
        String eventType,
        String login,
        String message
) {
}
