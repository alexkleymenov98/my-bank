package com.example.accounts.exception;

public class AccountNotFoundException extends RuntimeException {
  public AccountNotFoundException(String message) {
    super("Account not found: " + message);
  }
}
