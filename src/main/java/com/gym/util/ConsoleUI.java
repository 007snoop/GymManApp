package com.gym.util;

import com.gym.model.Admin;
import com.gym.model.Member;
import com.gym.model.Trainer;
import com.gym.model.User;
import com.gym.service.UserService;
import java.sql.Connection;
import java.util.Scanner;

public class ConsoleUI {
    private UserService userService;

    public ConsoleUI(Connection connection) {
        userService = new UserService(connection);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Gym Management System ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option between 1-3: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> registerUser(scanner);
                case 2 -> loginUser(scanner);
                case 3 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid entry, please enter a number between 1-3.");
            }

        }
    }

    private void registerUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter role (Admin, Trainer, Member): ");
        String role = scanner.nextLine();

        User newUser = null;
        switch (role.toLowerCase()) {
            case "admin" -> {
                newUser = new Admin(0, username, password, email, phoneNumber, address);
            }
            case "trainer" -> {
                newUser = new Trainer(0, username, password, email, phoneNumber, address);
            }
            case "member" -> {
                newUser = new Member(0, username, password, email, phoneNumber, address);
            }
            default -> {
                System.out.println("Invalid role.");
            }
        }

        try {
            if (newUser != null) {
                userService.register(newUser);
                System.out.println("User registered successfully!");
            }
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void loginUser(Scanner scanner) {
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = userService.getUserByUsername(username);
            if (user != null && userService.verifyPass(password, user.getPassword())) {
                System.out.println("Login successful! Welcome, " + user.getRole() + " " + user.getUsername());
                // role menu addon here
            } else {
                System.out.println("Invalid user or pass.");
            }
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }
}
