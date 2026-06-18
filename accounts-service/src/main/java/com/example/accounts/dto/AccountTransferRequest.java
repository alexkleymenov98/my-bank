package com.example.accounts.dto;

import java.math.BigDecimal;

public record AccountTransferRequest(
        String login,
        String target,
        BigDecimal amount
) {
}
