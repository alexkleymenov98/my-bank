package ru.yandex.practicum.mybankfront.dto;

import java.time.LocalDate;

public record AccountUpdate(
        String name,
        LocalDate birthdate
) {
}
