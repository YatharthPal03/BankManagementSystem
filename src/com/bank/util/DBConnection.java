package com.bank.util;
// These are helper classes used by many parts of the project.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Database URL
    private static final String URL =
            "jdbc:mysql://localhost:3306/bankdb";

    // MySQL Username
    private static final String USERNAME =
            "root";


    private static final String PASSWORD =
            System.getenv("DB_PASSWORD");


    // Method to establish and return a connection
    public static Connection getConnection() {

        Connection connection = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(
                    URL,
                    USERNAME,
                    PASSWORD
            );

        } catch (ClassNotFoundException e) {

            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();

        } catch (SQLException e) {

            System.out.println("Database Connection Failed!");
            e.printStackTrace();
        }

        return connection;
    }
}