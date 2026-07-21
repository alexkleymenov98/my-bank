package ru.yandex.practicum.mybankfront.dto;

import java.math.BigDecimal;

public record TransferRequest(
        String target,
        BigDecimal amount
) {
}
