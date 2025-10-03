package com.bankmanagement.service;

import com.bankmanagement.dto.UserDTO;
import com.bankmanagement.exception.UserNotFoundException;
import com.bankmanagement.model.User;
import com.bankmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO("Test User", "newuser@example.com", "9876543210");
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        User createdUser = userService.createUser(userDTO, "password");

        assertNotNull(createdUser);
        assertEquals("Test User", createdUser.getName());
        assertEquals("encodedPassword", createdUser.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_Failure_EmailExists() {
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(userDTO, "password");
        });

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_Success() {
        User existingUser = new User("Old Name", "user@example.com", "pass", "1234567890");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserDTO updateInfo = new UserDTO("New Name", "user@example.com", "9876543210");

        User updatedUser = userService.updateUser(1L, updateInfo);

        assertEquals("New Name", updatedUser.getName());
        assertEquals("9876543210", updatedUser.getPhone());
        verify(userRepository, times(1)).save(existingUser);
    }
}

