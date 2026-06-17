package com.example.notification.dto;


public record NotificationRequest(
        String login,
        String message
) {
}
