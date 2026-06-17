package com.example.transfer.dto;

import java.math.BigDecimal;

public record TransferRequestAccounts(
        String login,
        String target,
        BigDecimal amount
) {
}
