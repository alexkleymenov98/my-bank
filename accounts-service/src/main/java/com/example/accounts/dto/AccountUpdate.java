package com.example.accounts.dto;

import java.time.LocalDate;

public record AccountUpdate(
        String name,
        LocalDate birthdate
) {
}
