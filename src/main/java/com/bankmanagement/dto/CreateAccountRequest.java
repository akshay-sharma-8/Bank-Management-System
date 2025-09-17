package com.bankmanagement.dto;

import com.bankmanagement.model.enums.AccountType;

public class CreateAccountRequest {
    private Long userId;
    private AccountType accountType;

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public AccountType getAccountType() {
        return accountType;
    }
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}