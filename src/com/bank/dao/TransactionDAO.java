package com.bank.dao;

import com.bank.model.Transaction;
import com.bank.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionDAO {

    public void viewTransactions(String accountNumber) {

        String sql =
                "SELECT * FROM transactions " +
                        "WHERE account_number = ? " +
                        "ORDER BY transaction_date DESC";

        try {

            Connection connection = DBConnection.getConnection();

            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);

            preparedStatement.setString(1, accountNumber);

            ResultSet resultSet =
                    preparedStatement.executeQuery();

            System.out.println("\n========== TRANSACTION HISTORY ==========\n");

            boolean found = false;

            while (resultSet.next()) {

                found = true;

                System.out.println(
                        "Transaction ID : " +
                                resultSet.getInt("transaction_id")
                );

                System.out.println(
                        "Type           : " +
                                resultSet.getString("transaction_type")
                );

                BigDecimal amount =
                        resultSet.getBigDecimal("amount");

                System.out.println(
                        "Amount         : ₹" + amount
                );

                System.out.println(
                        "Date           : " +
                                resultSet.getTimestamp("transaction_date")
                );

                System.out.println("---------------------------------------");
            }

            if (!found) {

                System.out.println(
                        "No transactions found for this account."
                );
            }

            connection.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}