package com.bankmanagement.exception;

public class AccountNotFoundException extends RuntimeException {
    private static final String message = "not found";

    public AccountNotFoundException() {
        super(message);
    }

    public AccountNotFoundException(String accountNumber) {
        super("Account not found with number: " + accountNumber);
    }

    public AccountNotFoundException(Long accountId) {
        super("Account not found with ID: " + accountId);
    }
}