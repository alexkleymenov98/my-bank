package com.example.cash.dto;

import java.math.BigDecimal;

public record AccountTransferRequest(
        String login,
        String target,
        BigDecimal amount
) {
}
