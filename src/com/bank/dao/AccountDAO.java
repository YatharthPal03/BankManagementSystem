package com.bank.dao;

import com.bank.model.Account;
import com.bank.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    public boolean createAccount(Account account) {

        String sql =
                "INSERT INTO accounts(account_number, customer_id, account_type, balance) " +
                        "VALUES (?, ?, ?, ?)";

        /*
         * TRY-WITH-RESOURCES
         *
         * Connection and PreparedStatement are automatically
         * closed when the try block finishes.
         *
         * We no longer need:
         *
         * connection.close();
         */

        try (
                Connection connection = DBConnection.getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(1, account.getAccountNumber());
            preparedStatement.setInt(2, account.getCustomerId());
            preparedStatement.setString(3, account.getAccountType());

            // BigDecimal is used because balance represents money.
            preparedStatement.setBigDecimal(4, account.getBalance());

            int rows = preparedStatement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }


    public void viewAccounts() {

        String sql = "SELECT * FROM accounts";

        /*
         * Three resources are used here:
         *
         * Connection
         * PreparedStatement
         * ResultSet
         *
         * All three are automatically closed.
         */

        try (
                Connection connection = DBConnection.getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        preparedStatement.executeQuery()
        ) {

            System.out.println("\n========== ACCOUNT LIST ==========\n");

            boolean found = false;

            while (resultSet.next()) {

                found = true;

                System.out.println(
                        "Account ID     : " +
                                resultSet.getInt("account_id")
                );

                System.out.println(
                        "Account Number : " +
                                resultSet.getString("account_number")
                );

                System.out.println(
                        "Customer ID    : " +
                                resultSet.getInt("customer_id")
                );

                System.out.println(
                        "Account Type   : " +
                                resultSet.getString("account_type")
                );

                // Read DECIMAL from MySQL as BigDecimal.
                System.out.println(
                        "Balance        : ₹" +
                                resultSet.getBigDecimal("balance")
                );

                System.out.println("---------------------------------------");
            }

            if (!found) {

                System.out.println("No accounts found.");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }


    public boolean deposit(String accountNumber, BigDecimal amount) {

        String updateSQL =
                "UPDATE accounts " +
                        "SET balance = balance + ? " +
                        "WHERE account_number = ?";

        String transactionSQL =
                "INSERT INTO transactions " +
                        "(account_number, transaction_type, amount) " +
                        "VALUES (?, 'DEPOSIT', ?)";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement updateStatement =
                        connection.prepareStatement(updateSQL);
                PreparedStatement transactionStatement =
                        connection.prepareStatement(transactionSQL)
        ) {

            // Start transaction
            connection.setAutoCommit(false);

            // Update account balance
            updateStatement.setBigDecimal(1, amount);
            updateStatement.setString(2, accountNumber);

            int rows = updateStatement.executeUpdate();

            // Account doesn't exist
            if (rows == 0) {
                connection.rollback();
                return false;
            }

            // Record transaction
            transactionStatement.setString(1, accountNumber);
            transactionStatement.setBigDecimal(2, amount);

            transactionStatement.executeUpdate();

            // Everything succeeded
            connection.commit();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    public boolean withdraw(String accountNumber, BigDecimal amount) {

        String updateSQL =
                "UPDATE accounts " +
                        "SET balance = balance - ? " +
                        "WHERE account_number = ? AND balance >= ?";

        String transactionSQL =
                "INSERT INTO transactions " +
                        "(account_number, transaction_type, amount) " +
                        "VALUES (?, 'WITHDRAW', ?)";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement updateStatement =
                        connection.prepareStatement(updateSQL);
                PreparedStatement transactionStatement =
                        connection.prepareStatement(transactionSQL)
        ) {

            // Start transaction
            connection.setAutoCommit(false);

            // Reduce account balance
            updateStatement.setBigDecimal(1, amount);
            updateStatement.setString(2, accountNumber);
            updateStatement.setBigDecimal(3, amount);

            int rows = updateStatement.executeUpdate();

            // Account doesn't exist OR insufficient balance
            if (rows == 0) {
                connection.rollback();
                return false;
            }

            // Record transaction
            transactionStatement.setString(1, accountNumber);
            transactionStatement.setBigDecimal(2, amount);

            transactionStatement.executeUpdate();

            // Everything succeeded
            connection.commit();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    public boolean transfer(String fromAccount,
                            String toAccount,
                            BigDecimal amount) {

        String withdrawSQL =
                "UPDATE accounts " +
                        "SET balance = balance - ? " +
                        "WHERE account_number = ? AND balance >= ?";

        String depositSQL =
                "UPDATE accounts " +
                        "SET balance = balance + ? " +
                        "WHERE account_number = ?";

        String transferOutSQL =
                "INSERT INTO transactions " +
                        "(account_number, transaction_type, amount) " +
                        "VALUES (?, 'TRANSFER_OUT', ?)";

        String transferInSQL =
                "INSERT INTO transactions " +
                        "(account_number, transaction_type, amount) " +
                        "VALUES (?, 'TRANSFER_IN', ?)";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement withdrawStatement =
                        connection.prepareStatement(withdrawSQL);
                PreparedStatement depositStatement =
                        connection.prepareStatement(depositSQL);
                PreparedStatement transferOutStatement =
                        connection.prepareStatement(transferOutSQL);
                PreparedStatement transferInStatement =
                        connection.prepareStatement(transferInSQL)
        ) {

            // Start transaction
            connection.setAutoCommit(false);

            // 1. Withdraw money from sender
            withdrawStatement.setBigDecimal(1, amount);
            withdrawStatement.setString(2, fromAccount);
            withdrawStatement.setBigDecimal(3, amount);

            int withdrawRows =
                    withdrawStatement.executeUpdate();

            // Sender doesn't exist OR insufficient balance
            if (withdrawRows == 0) {
                connection.rollback();
                return false;
            }

            // 2. Add money to receiver
            depositStatement.setBigDecimal(1, amount);
            depositStatement.setString(2, toAccount);

            int depositRows =
                    depositStatement.executeUpdate();

            // Receiver doesn't exist
            if (depositRows == 0) {
                connection.rollback();
                return false;
            }

            // 3. Record sender transaction
            transferOutStatement.setString(1, fromAccount);
            transferOutStatement.setBigDecimal(2, amount);

            transferOutStatement.executeUpdate();

            // 4. Record receiver transaction
            transferInStatement.setString(1, toAccount);
            transferInStatement.setBigDecimal(2, amount);

            transferInStatement.executeUpdate();

            // Everything succeeded
            connection.commit();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    public BigDecimal getBalance(String accountNumber) {

        String sql =
                "SELECT balance " +
                        "FROM accounts " +
                        "WHERE account_number = ?";

        /*
         * Connection, PreparedStatement and ResultSet
         * are automatically closed.
         */

        try (
                Connection connection = DBConnection.getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(1, accountNumber);

            try (
                    ResultSet resultSet =
                            preparedStatement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSet.getBigDecimal("balance");
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        // null means the account was not found.
        return null;
    }

    // Checks whether an account number is already in use
    public boolean accountExists(String accountNumber) {

        String sql =
                "SELECT account_number " +
                        "FROM accounts " +
                        "WHERE account_number = ?";

        try (
                Connection connection = DBConnection.getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(1, accountNumber);

            try (
                    ResultSet resultSet =
                            preparedStatement.executeQuery()
            ) {

                return resultSet.next();
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }
}