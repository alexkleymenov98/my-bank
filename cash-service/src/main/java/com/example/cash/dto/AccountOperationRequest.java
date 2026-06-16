package com.example.cash.dto;

import java.math.BigDecimal;

public record AccountOperationRequest(
        String login,
        BigDecimal amount
) {
}
