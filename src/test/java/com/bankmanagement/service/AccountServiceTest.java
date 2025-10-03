package com.bankmanagement.service;

import com.bankmanagement.exception.UserNotFoundException;
import com.bankmanagement.model.Account;
import com.bankmanagement.model.User;
import com.bankmanagement.model.enums.AccountType;
import com.bankmanagement.repository.AccountRepository;
import com.bankmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountService accountService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("Test User", "test@example.com", "password", "9876543210");
        testUser.setId(1L);
    }

    @Test
    void testCreateAccount_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account createdAccount = accountService.createAccount(1L, AccountType.SAVINGS);

        assertNotNull(createdAccount);
        assertEquals(testUser, createdAccount.getUser());
        assertEquals(AccountType.SAVINGS, createdAccount.getAccountType());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testCreateAccount_Failure_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            accountService.createAccount(1L, AccountType.SAVINGS);
        });

        verify(accountRepository, never()).save(any(Account.class));
    }
}