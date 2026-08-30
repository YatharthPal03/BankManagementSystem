package com.bank.dao;

import com.bank.model.Customer;
import com.bank.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    // =========================================================
    // ADD CUSTOMER
    // =========================================================

    public boolean addCustomer(Customer customer) {

        String sql =
                "INSERT INTO customers(name, email, phone, address) " +
                        "VALUES (?, ?, ?, ?)";

        /*
         * BEFORE:
         *
         * Connection connection = DBConnection.getConnection();
         * PreparedStatement preparedStatement =
         *         connection.prepareStatement(sql);
         *
         * ...
         *
         * connection.close();
         *
         *
         * PROBLEM:
         * If an exception happened before connection.close(),
         * the connection could remain open.
         *
         *
         * NOW:
         * We use try-with-resources.
         *
         * Java automatically closes the Connection and
         * PreparedStatement when this try block finishes.
         */

        try (
                Connection connection = DBConnection.getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getPhone());
            preparedStatement.setString(4, customer.getAddress());

            int rows = preparedStatement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }


    // =========================================================
    // VIEW CUSTOMERS
    // =========================================================

    public void viewCustomers() {

        String sql = "SELECT * FROM customers";

        /*
         * ResultSet is also a resource that needs to be closed.
         *
         * So now we put ALL THREE inside try-with-resources:
         *
         * Connection
         * PreparedStatement
         * ResultSet
         *
         * Java will automatically close all of them.
         */

        try (
                Connection connection = DBConnection.getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        preparedStatement.executeQuery()
        ) {

            System.out.println("\n========== CUSTOMER LIST ==========\n");

            boolean found = false;

            while (resultSet.next()) {

                found = true;

                System.out.println(
                        "Customer ID : " +
                                resultSet.getInt("customer_id")
                );

                System.out.println(
                        "Name        : " +
                                resultSet.getString("name")
                );

                System.out.println(
                        "Email       : " +
                                resultSet.getString("email")
                );

                System.out.println(
                        "Phone       : " +
                                resultSet.getString("phone")
                );

                System.out.println(
                        "Address     : " +
                                resultSet.getString("address")
                );

                System.out.println("---------------------------------------");
            }

            if (!found) {

                System.out.println("No customers found.");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }


    // =========================================================
    // UPDATE CUSTOMER
    // =========================================================

    public boolean updateCustomer(Customer customer) {

        String sql =
                "UPDATE customers " +
                        "SET name = ?, email = ?, phone = ?, address = ? " +
                        "WHERE customer_id = ?";

        /*
         * Same improvement here:
         *
         * Connection and PreparedStatement are automatically
         * closed by try-with-resources.
         */

        try (
                Connection connection = DBConnection.getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getPhone());
            preparedStatement.setString(4, customer.getAddress());
            preparedStatement.setInt(5, customer.getCustomerId());

            int rows = preparedStatement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }


    // =========================================================
    // DELETE CUSTOMER
    // =========================================================

    public boolean deleteCustomer(int customerId) {

        String sql =
                "DELETE FROM customers WHERE customer_id = ?";

        /*
         * Again:
         *
         * BEFORE:
         * We manually called connection.close().
         *
         * NOW:
         * try-with-resources automatically handles it.
         */

        try (
                Connection connection = DBConnection.getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setInt(1, customerId);

            int rows = preparedStatement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }

    // Checks whether a customer exists before creating an account
    public boolean customerExists(int customerId) {

        String sql =
                "SELECT customer_id FROM customers WHERE customer_id = ?";

        try (
                Connection connection = DBConnection.getConnection();

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setInt(1, customerId);

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