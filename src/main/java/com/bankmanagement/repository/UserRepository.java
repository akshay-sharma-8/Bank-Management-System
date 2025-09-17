package com.bankmanagement.repository;

import com.bankmanagement.model.User;
import com.bankmanagement.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    List<User> findByStatus(UserStatus status);
    List<User> findByNameContainingIgnoreCase(String name);
}