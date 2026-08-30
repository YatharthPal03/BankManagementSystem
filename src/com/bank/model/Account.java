package com.bank.model;
import java.math.BigDecimal;

public class Account {

    private int accountId;
    private String accountNumber;
    private int customerId;
    private String accountType;
    private BigDecimal balance;

    // Default constructor
    public Account() {

    }

    // Constructor for creating a new account
    public Account(String accountNumber, int customerId,
                   String accountType, BigDecimal balance) {

        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = balance;
    }

    // Constructor with account ID
    public Account(int accountId, String accountNumber,
                   int customerId, String accountType,
                   BigDecimal balance) {

        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = balance;
    }

    // Getters

    public int getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getAccountType() {
        return accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    // Setters

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {

        return "Account{" +
                "accountId=" + accountId +
                ", accountNumber='" + accountNumber + '\'' +
                ", customerId=" + customerId +
                ", accountType='" + accountType + '\'' +
                ", balance=" + balance +
                '}';
    }
}