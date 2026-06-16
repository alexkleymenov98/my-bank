package com.example.cash.dto;

import java.time.LocalDate;

public record AccountUpdate(
        String name,
        LocalDate birthdate
) {
}
