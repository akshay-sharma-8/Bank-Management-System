package com.bankmanagement.repository;

import com.bankmanagement.model.Transaction;
import com.bankmanagement.model.Account;
import com.bankmanagement.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionId(String transactionId);
    List<Transaction> findByFromAccount(Account account);
    List<Transaction> findByToAccount(Account account);
    List<Transaction> findByTransactionType(TransactionType transactionType);

    @Query("SELECT t FROM Transaction t WHERE t.fromAccount = ?1 OR t.toAccount = ?1 ORDER BY t.transactionDate DESC")
    List<Transaction> findByAccount(Account account);

    @Query("SELECT t FROM Transaction t WHERE t.fromAccount.id = ?1 OR t.toAccount.id = ?1 ORDER BY t.transactionDate DESC")
    List<Transaction> findByAccountId(Long accountId);
}