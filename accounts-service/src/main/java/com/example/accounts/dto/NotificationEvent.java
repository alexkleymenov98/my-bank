package com.example.accounts.dto;

public record NotificationEvent(
        String eventId,
        String eventType,
        String login,
        String message
) {
}
