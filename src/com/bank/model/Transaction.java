package com.bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private int transactionId;
    private String accountNumber;
    private String transactionType;
    private BigDecimal amount;
    private LocalDateTime transactionDate;

    public Transaction() {

    }

    public Transaction(String accountNumber,
                       String transactionType,
                       BigDecimal amount) {

        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    public Transaction(int transactionId,
                       String accountNumber,
                       String transactionType,
                       BigDecimal amount,
                       LocalDateTime transactionDate) {

        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {

        return "Transaction{" +
                "transactionId=" + transactionId +
                ", accountNumber='" + accountNumber + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                ", transactionDate=" + transactionDate +
                '}';
    }
}