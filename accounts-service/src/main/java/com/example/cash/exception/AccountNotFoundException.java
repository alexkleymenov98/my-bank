package com.example.cash.exception;

public class AccountNotFoundException extends RuntimeException {
  public AccountNotFoundException(String message) {
    super("Account not found: " + message);
  }
}
