package ru.yandex.practicum.mybankfront.dto;

import ru.yandex.practicum.mybankfront.controller.dto.CashAction;

public record CashRequest(
        int amount,
        CashAction action
) {
}
