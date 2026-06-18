package com.example.accounts.dto;

import java.math.BigDecimal;

public record AccountOperationRequest(
        String login,
        BigDecimal amount
) {
}
