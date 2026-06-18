package com.example.accounts.exception;

public class ValidationBalanceException extends RuntimeException {
    public ValidationBalanceException() {
        super("Недостаточно средств");
    }
}
