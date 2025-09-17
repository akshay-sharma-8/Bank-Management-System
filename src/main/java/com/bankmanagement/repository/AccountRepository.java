package com.bankmanagement.repository;

import com.bankmanagement.model.Account;
import com.bankmanagement.model.User;
import com.bankmanagement.model.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByUser(User user);
    List<Account> findByUserId(Long userId);
    List<Account> findByAccountType(AccountType accountType);
    List<Account> findByActive(boolean active);
    boolean existsByAccountNumber(String accountNumber);
}