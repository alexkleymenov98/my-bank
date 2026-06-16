package com.example.cash.exception;

public class ValidationBalanceException extends RuntimeException {
    public ValidationBalanceException() {
        super("Недостаточно средств");
    }
}
