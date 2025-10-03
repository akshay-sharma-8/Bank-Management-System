package com.bankmanagement.service;

import com.bankmanagement.repository.AccountRepository;
import com.bankmanagement.repository.AdminRepository;
import com.bankmanagement.repository.TransactionRepository;
import com.bankmanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void testGetDashboardStats() {
        when(userRepository.count()).thenReturn(10L);
        when(accountRepository.count()).thenReturn(20L);
        when(transactionRepository.count()).thenReturn(30L);
        when(accountRepository.findByActive(true)).thenReturn(java.util.Collections.nCopies(15, null));

        Map<String, Object> stats = adminService.getDashboardStats();

        assertEquals(10L, stats.get("totalUsers"));
        assertEquals(20L, stats.get("totalAccounts"));
        assertEquals(30L, stats.get("totalTransactions"));
        assertEquals(15, stats.get("activeAccounts"));
    }
}