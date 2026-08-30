package com.bank;

import com.bank.dao.CustomerDAO;
import com.bank.dao.TransactionDAO;
import com.bank.model.Customer;
import com.bank.model.Account;
import com.bank.dao.AccountDAO;
import com.bank.util.InputValidator;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();
        TransactionDAO transactionDAO = new TransactionDAO();

        while (true) {

            System.out.println("\n========== BANK MANAGEMENT SYSTEM ==========");
            System.out.println("1. Add Customer");
            System.out.println("2. View Customers");
            System.out.println("3. Update Customer");
            System.out.println("4. Delete Customer");
            System.out.println("5. Create Account");
            System.out.println("6. View Accounts");
            System.out.println("7. Deposit Money");
            System.out.println("8. Withdraw Money");
            System.out.println("9. Transfer Money");
            System.out.println("10. Check Balance");
            System.out.println("11. Transaction History");
            System.out.println("12. Exit");
            System.out.print("Enter your choice: ");

            int choice;

            if (scanner.hasNextInt()) {

                choice = scanner.nextInt();
                scanner.nextLine();

            } else {

                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {

                case 1: {

                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();

                    if (!InputValidator.isValidName(name)) {
                        System.out.println("Invalid name!");
                        break;
                    }

                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();

                    if (!InputValidator.isValidEmail(email)) {
                        System.out.println("Invalid email!");
                        break;
                    }

                    System.out.print("Enter Phone: ");
                    String phone = scanner.nextLine();

                    if (!InputValidator.isValidPhone(phone)) {
                        System.out.println("Phone must contain exactly 10 digits!");
                        break;
                    }

                    System.out.print("Enter Address: ");
                    String address = scanner.nextLine();

                    Customer customer =
                            new Customer(name, email, phone, address);

                    if (customerDAO.addCustomer(customer)) {
                        System.out.println("Customer Added Successfully!");
                    } else {
                        System.out.println("Failed to Add Customer!");
                    }

                    break;
                }

                case 2: {

                    customerDAO.viewCustomers();

                    break;
                }

                case 3: {

                    System.out.print("Enter Customer ID to update: ");

                    if (!scanner.hasNextInt()) {

                        System.out.println("Invalid Customer ID! Please enter a number.");
                        scanner.nextLine();
                        break;
                    }

                    int customerId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter New Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter New Email: ");
                    String email = scanner.nextLine();

                    System.out.print("Enter New Phone: ");
                    String phone = scanner.nextLine();

                    System.out.print("Enter New Address: ");
                    String address = scanner.nextLine();

                    Customer customer =
                            new Customer(
                                    customerId,
                                    name,
                                    email,
                                    phone,
                                    address
                            );

                    if (customerDAO.updateCustomer(customer)) {
                        System.out.println("Customer Updated Successfully!");
                    } else {
                        System.out.println("Customer Not Found!");
                    }

                    break;
                }

                case 4: {

                    System.out.print("Enter Customer ID to delete: ");

                    if (!scanner.hasNextInt()) {

                        System.out.println("Invalid Customer ID! Please enter a number.");
                        scanner.nextLine();
                        break;
                    }

                    int customerId = scanner.nextInt();
                    scanner.nextLine();

                    if (customerDAO.deleteCustomer(customerId)) {
                        System.out.println("Customer Deleted Successfully!");
                    } else {
                        System.out.println("Customer Not Found!");
                    }

                    break;
                }

                case 5: {

                    System.out.print("Enter Account Number: ");
                    String accountNumber = scanner.nextLine();

                    // Validate account number
                    if (!InputValidator.isValidAccountNumber(accountNumber)) {

                        System.out.println("Account number must contain only digits!");
                        break;
                    }

                    // Check duplicate account number
                    if (accountDAO.accountExists(accountNumber)) {

                        System.out.println("Account number already exists!");
                        break;
                    }

                    System.out.print("Enter Customer ID: ");

                    if (!scanner.hasNextInt()) {

                        System.out.println("Invalid Customer ID! Please enter a number.");
                        scanner.nextLine();
                        break;
                    }

                    int customerId = scanner.nextInt();
                    scanner.nextLine();

                    // Check whether customer exists
                    if (!customerDAO.customerExists(customerId)) {

                        System.out.println("Customer Not Found!");
                        break;
                    }

                    System.out.print("Enter Account Type (Savings/Current): ");
                    String accountType = scanner.nextLine();

                    // Validate account type
                    if (!InputValidator.isValidAccountType(accountType)) {

                        System.out.println("Account type must be Savings or Current!");
                        break;
                    }

                    System.out.print("Enter Initial Balance: ");

                    if (!scanner.hasNextBigDecimal()) {

                        System.out.println("Invalid amount! Please enter a valid number.");
                        scanner.nextLine();
                        break;
                    }

                    BigDecimal balance = scanner.nextBigDecimal();
                    scanner.nextLine();

                    // Validate initial balance
                    if (!InputValidator.isValidAmount(balance)) {

                        System.out.println("Initial balance must be greater than 0!");
                        break;
                    }

                    Account account = new Account(
                            accountNumber,
                            customerId,
                            accountType,
                            balance
                    );

                    if (accountDAO.createAccount(account)) {

                        System.out.println("Account Created Successfully!");

                    } else {

                        System.out.println("Failed to Create Account!");
                    }

                    break;
                }

                case 6: {

                    accountDAO.viewAccounts();

                    break;
                }

                case 7: {

                    System.out.print("Enter Account Number: ");
                    String accountNumber = scanner.nextLine();

                    // Validate account number before accessing the database
                    if (!InputValidator.isValidAccountNumber(accountNumber)) {

                        System.out.println("Account number must contain only digits!");
                        break;
                    }

                    System.out.print("Enter Deposit Amount: ");

                    if (!scanner.hasNextBigDecimal()) {

                        System.out.println("Invalid amount! Please enter a valid number.");
                        scanner.nextLine();
                        break;
                    }

                    BigDecimal amount = scanner.nextBigDecimal();
                    scanner.nextLine();

                    if (!InputValidator.isValidAmount(amount)) {
                        System.out.println("Amount must be greater than 0!");
                        break;
                    }

                    if (accountDAO.deposit(accountNumber, amount)) {
                        System.out.println("Money Deposited Successfully!");
                    } else {
                        System.out.println("Account Not Found!");
                    }

                    break;
                }

                case 8: {

                    System.out.print("Enter Account Number: ");
                    String accountNumber = scanner.nextLine();

                    // Validate account number
                    if (!InputValidator.isValidAccountNumber(accountNumber)) {

                        System.out.println("Account number must contain only digits!");
                        break;
                    }

                    System.out.print("Enter Withdrawal Amount: ");

                    if (!scanner.hasNextBigDecimal()) {

                        System.out.println("Invalid amount! Please enter a valid number.");
                        scanner.nextLine();
                        break;
                    }

                    BigDecimal amount = scanner.nextBigDecimal();
                    scanner.nextLine();

                    if (!InputValidator.isValidAmount(amount)) {
                        System.out.println("Amount must be greater than 0!");
                        break;
                    }

                    if (accountDAO.withdraw(accountNumber, amount)) {
                        System.out.println("Money Withdrawn Successfully!");
                    } else {
                        System.out.println(
                                "Withdrawal Failed! Check account number or balance."
                        );
                    }

                    break;
                }

                case 9: {

                    System.out.print("Enter Sender Account Number: ");
                    String fromAccount = scanner.nextLine();

                    // Validate sender account number
                    if (!InputValidator.isValidAccountNumber(fromAccount)) {

                        System.out.println("Sender account number must contain only digits!");
                        break;
                    }

                    System.out.print("Enter Receiver Account Number: ");
                    String toAccount = scanner.nextLine();

                    // Validate receiver account number
                    if (!InputValidator.isValidAccountNumber(toAccount)) {

                        System.out.println("Receiver account number must contain only digits!");
                        break;
                    }

                    System.out.print("Enter Transfer Amount: ");

                    if (!scanner.hasNextBigDecimal()) {

                        System.out.println("Invalid amount! Please enter a valid number.");
                        scanner.nextLine();
                        break;
                    }

                    BigDecimal amount = scanner.nextBigDecimal();
                    scanner.nextLine();

                    if (!InputValidator.isValidAmount(amount)) {
                        System.out.println("Transfer amount must be greater than 0!");
                        break;
                    }

                    if (fromAccount.equals(toAccount)) {
                        System.out.println(
                                "Sender and receiver accounts cannot be the same!"
                        );
                        break;
                    }

                    if (accountDAO.transfer(fromAccount, toAccount, amount)) {
                        System.out.println("Transfer Successful!");
                    } else {
                        System.out.println(
                                "Transfer Failed! Check account numbers or balance."
                        );
                    }

                    break;
                }

                case 10: {

                    System.out.print("Enter Account Number: ");
                    String accountNumber = scanner.nextLine();

                    // Validate account number
                    if (!InputValidator.isValidAccountNumber(accountNumber)) {

                        System.out.println("Account number must contain only digits!");
                        break;
                    }

                    BigDecimal balance =
                            accountDAO.getBalance(accountNumber);

                    if (balance == null) {

                        System.out.println("Account Not Found!");

                    } else {

                        System.out.println(
                                "\n========== ACCOUNT BALANCE =========="
                        );

                        System.out.println(
                                "Account Number : " + accountNumber
                        );

                        System.out.println(
                                "Current Balance: ₹" + balance
                        );

                        System.out.println(
                                "====================================="
                        );
                    }

                    break;
                }

                case 11: {

                    System.out.print("Enter Account Number: ");
                    String accountNumber = scanner.nextLine();

                    // Validate account number
                    if (!InputValidator.isValidAccountNumber(accountNumber)) {

                        System.out.println("Account number must contain only digits!");
                        break;
                    }

                    transactionDAO.viewTransactions(accountNumber);

                    break;
                }

                case 12: {

                    System.out.println("Thank You!");
                    scanner.close();
                    System.exit(0);

                    break;
                }

                default: {

                    System.out.println("Invalid Choice!");

                }
            }
        }
    }
}