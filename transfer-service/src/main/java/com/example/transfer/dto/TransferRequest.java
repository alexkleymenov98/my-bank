package com.example.transfer.dto;

import java.math.BigDecimal;

public record TransferRequest(
        String target,
        BigDecimal amount
) {
}
