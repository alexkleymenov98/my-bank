package com.example.cash.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CashOperationRequest(
        @Positive(message = "Сумма должна быть положительной")
        BigDecimal amount,
        @NotNull(message = "Action не может быть null")
        CashAction action
) {
}
