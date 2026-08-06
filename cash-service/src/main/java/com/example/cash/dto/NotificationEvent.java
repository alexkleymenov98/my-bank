package com.example.cash.dto;

public record NotificationEvent(
        String eventId,
        String eventType,
        String login,
        String message
) {
}
