package com.bankmanagement.service;

import com.bankmanagement.model.Admin;
import com.bankmanagement.repository.AdminRepository;
import com.bankmanagement.repository.UserRepository;
import com.bankmanagement.repository.AccountRepository;
import com.bankmanagement.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Admin createAdmin(String username, String email, String password) {
        if (adminRepository.existsByEmail(email)) {
            throw new RuntimeException("Admin email already exists");
        }

        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));

        return adminRepository.save(admin);
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalAccounts", accountRepository.count());
        stats.put("totalTransactions", transactionRepository.count());
        stats.put("activeAccounts", accountRepository.findByActive(true).size());
        return stats;
    }
}