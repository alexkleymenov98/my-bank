package com.example.transfer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank
        String target,
        @Positive(message = "Сумма должны быть положительной")
        BigDecimal amount
) {
}
