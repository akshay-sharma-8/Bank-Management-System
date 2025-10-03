package com.bankmanagement.util;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilTest {

    @Test
    void testIsValidEmail() {
        assertTrue(ValidationUtil.isValidEmail("test@example.com"));
        assertTrue(ValidationUtil.isValidEmail("test.name@example.co.uk"));
        assertFalse(ValidationUtil.isValidEmail("test@example"));
        assertFalse(ValidationUtil.isValidEmail("test.com"));
        assertFalse(ValidationUtil.isValidEmail(null));
    }

    @Test
    void testIsValidPhone() {
        assertTrue(ValidationUtil.isValidPhone("9876543210"));
        assertFalse(ValidationUtil.isValidPhone("1234567890")); // Doesn't start with 6-9
        assertFalse(ValidationUtil.isValidPhone("987654321"));  // Too short
        assertFalse(ValidationUtil.isValidPhone("98765432100")); // Too long
        assertFalse(ValidationUtil.isValidPhone(null));
    }

    @Test
    void testIsValidAmount() {
        assertTrue(ValidationUtil.isValidAmount(new BigDecimal("100.00")));
        assertTrue(ValidationUtil.isValidAmount(new BigDecimal("0.01")));
        assertFalse(ValidationUtil.isValidAmount(new BigDecimal("0.00")));
        assertFalse(ValidationUtil.isValidAmount(new BigDecimal("-100.00")));
        assertFalse(ValidationUtil.isValidAmount(null));
    }

    @Test
    void testIsValidPassword() {
        assertTrue(ValidationUtil.isValidPassword("password"));
        assertTrue(ValidationUtil.isValidPassword("123456"));
        assertFalse(ValidationUtil.isValidPassword("12345")); // Too short
        assertFalse(ValidationUtil.isValidPassword(null));
    }

    @Test
    void testIsNotEmpty() {
        assertTrue(ValidationUtil.isNotEmpty("test"));
        assertFalse(ValidationUtil.isNotEmpty("  "));
        assertFalse(ValidationUtil.isNotEmpty(""));
        assertFalse(ValidationUtil.isNotEmpty(null));
    }
}