package com.bankmanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.bankmanagement.repository")
public class DatabaseConfig {
    // Additional database configuration can be added here if needed
}