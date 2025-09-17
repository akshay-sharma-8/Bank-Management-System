package com.bankmanagement.service;

import com.bankmanagement.model.Admin;
import com.bankmanagement.model.User;
import com.bankmanagement.repository.AdminRepository;
import com.bankmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Map<String, Object> authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
            response.put("role", "USER");
            return response;
        }

        throw new RuntimeException("Invalid credentials");
    }

    public Map<String, Object> authenticateAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmail(email).orElse(null);

        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", admin.getId());
            response.put("username", admin.getUsername());
            response.put("email", admin.getEmail());
            response.put("role", "ADMIN");
            return response;
        }

        throw new RuntimeException("Invalid admin credentials");
    }
}