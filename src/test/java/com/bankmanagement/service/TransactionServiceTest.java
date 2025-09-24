package com.bankmanagement.service;

import com.bankmanagement.exception.AccountNotFoundException;
import com.bankmanagement.exception.InsufficientBalanceException;
import com.bankmanagement.model.Account;
import com.bankmanagement.model.Transaction;
import com.bankmanagement.repository.AccountRepository;
import com.bankmanagement.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        // Create a re-usable test account for our tests
        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setAccountNumber("ACC123456");
        testAccount.setBalance(new BigDecimal("5000.00"));
        testAccount.setMinimumBalance(new BigDecimal("1000.00")); //
        testAccount.setActive(true);
    }

    @Test
    void testWithdraw_Success() {
        // Arrange
        String accountNumber = "ACC123456";
        BigDecimal withdrawAmount = new BigDecimal("2000.00");

        // Mock the repository to return our test account
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(testAccount));
        // Mock the repository save calls
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        // Act
        Transaction transaction = transactionService.withdraw(accountNumber, withdrawAmount, "Test withdrawal"); //

        // Assert
        assertNotNull(transaction);
        // Verify the new balance is correct (5000 - 2000 = 3000)
        assertEquals(new BigDecimal("3000.00"), testAccount.getBalance());
        // Verify that the account and transaction were saved
        verify(accountRepository, times(1)).save(testAccount);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testWithdraw_Failure_InsufficientBalance() {
        // Arrange
        String accountNumber = "ACC123456";
        BigDecimal withdrawAmount = new BigDecimal("6000.00"); // More than the 5000 balance

        // Mock the repository to return our test account
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(testAccount));

        // Act & Assert
        // Check that the correct exception is thrown
        InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> transactionService.withdraw(accountNumber, withdrawAmount, "Test withdrawal"), //
                "Should have thrown InsufficientBalanceException"
        );

        assertTrue(exception.getMessage().contains("Insufficient balance")); //
        // Verify that no save operations occurred
        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testWithdraw_Failure_MinimumBalanceBreach() {
        // Arrange
        String accountNumber = "ACC123456";
        // This withdrawal (4500) would leave 500, which is below the 1000 minimum balance
        BigDecimal withdrawAmount = new BigDecimal("4500.00");

        // Mock the repository to return our test account
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(testAccount));

        // Act & Assert
        InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> transactionService.withdraw(accountNumber, withdrawAmount, "Test withdrawal"), //
                "Should have thrown InsufficientBalanceException"
        );

        assertTrue(exception.getMessage().contains("Minimum balance required")); //
        // Verify that no save operations occurred
        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testWithdraw_Failure_AccountNotFound() {
        // Arrange
        String accountNumber = "NOT_FOUND";
        BigDecimal withdrawAmount = new BigDecimal("100.00");

        // Mock the repository to return an empty Optional
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                AccountNotFoundException.class,
                () -> transactionService.withdraw(accountNumber, withdrawAmount, "Test withdrawal"), //
                "Should have thrown AccountNotFoundException"
        );
    }
}