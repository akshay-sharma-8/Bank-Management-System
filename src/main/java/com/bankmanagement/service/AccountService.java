package com.bankmanagement.service;

import com.bankmanagement.dto.AccountDTO;
import com.bankmanagement.exception.AccountNotFoundException;
import com.bankmanagement.exception.UserNotFoundException;
import com.bankmanagement.model.Account;
import com.bankmanagement.model.User;
import com.bankmanagement.model.enums.AccountType;
import com.bankmanagement.repository.AccountRepository;
import com.bankmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public Account createAccount(Long userId, AccountType accountType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setAccountType(accountType);
        account.setUser(user);

        return accountRepository.save(account);
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    public List<AccountDTO> getUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void updateAccountStatus(Long accountId, boolean active) {
        Account account = getAccountById(accountId);
        account.setActive(active);
        accountRepository.save(account);
    }

    // New Delete Method
    public void deleteAccount(Long accountId) {
        Account account = getAccountById(accountId);
        accountRepository.delete(account);
    }

    private String generateAccountNumber() {
        String accountNumber;
        do {
            accountNumber = "ACC" + (100000000L + new Random().nextLong() % 900000000L);
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    private AccountDTO convertToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        dto.setMinimumBalance(account.getMinimumBalance());
        dto.setActive(account.isActive());
        dto.setUserId(account.getUser().getId());
        dto.setUserName(account.getUser().getName());
        return dto;
    }
}