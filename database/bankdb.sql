-- Bank Management System Database
-- MySQL

CREATE DATABASE IF NOT EXISTS bankdb;

USE bankdb;


-- ==============================
-- CUSTOMERS TABLE
-- ==============================

CREATE TABLE IF NOT EXISTS customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(200)
);


-- ==============================
-- ACCOUNTS TABLE
-- ==============================

CREATE TABLE IF NOT EXISTS accounts (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(20) NOT NULL UNIQUE,
    customer_id INT NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,

    CONSTRAINT fk_accounts_customer
        FOREIGN KEY (customer_id)
        REFERENCES customers(customer_id)
);


-- ==============================
-- TRANSACTIONS TABLE
-- ==============================

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(20) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);