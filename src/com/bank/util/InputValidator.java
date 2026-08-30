package com.bank.util;

import java.math.BigDecimal;

public class InputValidator {

    public static boolean isValidAmount(BigDecimal amount) {

        return amount != null &&
                amount.compareTo(BigDecimal.ZERO) > 0;
    }

    // Checks whether account number contains only digits
    public static boolean isValidAccountNumber(String accountNumber) {

        return accountNumber != null &&
                accountNumber.matches("\\d+");
    }

    public static boolean isValidName(String name) {

        return name != null &&
                !name.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {

        return email != null &&
                email.contains("@") &&
                email.contains(".");
    }

    public static boolean isValidPhone(String phone) {

        return phone != null &&
                phone.matches("\\d{10}");
    }


    // Checks whether the account type is valid
    public static boolean isValidAccountType(String accountType) {

        return accountType != null &&
                (accountType.equalsIgnoreCase("savings") ||
                        accountType.equalsIgnoreCase("current"));
    }
}
