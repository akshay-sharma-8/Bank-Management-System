package com.bankmanagement.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final String PHONE_PATTERN = "^[6-9]\\d{9}$";
    private static final String PAN_PATTERN = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$";

    public static boolean isValidEmail(String email) {
        return email != null && Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && Pattern.compile(PHONE_PATTERN).matcher(phone).matches();
    }

    public static boolean isValidPAN(String pan) {
        return pan != null && Pattern.compile(PAN_PATTERN).matcher(pan).matches();
    }

    public static boolean isValidAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}