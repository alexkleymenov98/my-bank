package com.example.cash.dto;

import java.math.BigDecimal;

public record CashOperationRequest(
        BigDecimal amount,
        CashAction action
) {
}
