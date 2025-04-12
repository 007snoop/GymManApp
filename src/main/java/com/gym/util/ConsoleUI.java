package com.gym.util;

import com.gym.dao.MembershipDAO;
import com.gym.model.*;
import com.gym.service.MembershipService;
import com.gym.service.UserService;
import com.gym.service.WorkoutClassService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner;
    private final UserService userService;
    private final MembershipService membershipService;
    private final WorkoutClassService workoutClassService;

    public ConsoleUI(UserService userService, MembershipService membershipService, WorkoutClassService workoutClassService) {
        this.userService = userService;
        this.membershipService = membershipService;
        this.workoutClassService = workoutClassService;
        this.scanner = new Scanner(System.in);
    }

    public ConsoleUI(Connection connection) {
        this.userService = new UserService(connection);
        this.membershipService = new MembershipService(new MembershipDAO(connection));
        this.scanner = new Scanner(System.in);
        this.workoutClassService = new WorkoutClassService(connection);
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
            case "admin" ->
                newUser = new Admin(0, username, password, email, phoneNumber, address);

            case "trainer" ->
                newUser = new Trainer(0, username, password, email, phoneNumber, address);

            case "member" ->
                newUser = new Member(0, username, password, email, phoneNumber, address);

            default ->
                System.out.println("Invalid role.");

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

                // check role here and display menu based on role
                switch (user.getRole().toLowerCase()) {
                    case "admin" -> adminMenu();
                    case "trainer" -> trainerMenu(user);
                    case "member" -> memberMenu(user);
                    default -> System.out.println("Unknown role: " + user.getRole());
                }
            } else {
                System.out.println("Invalid user or pass.");
            }
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private void memberMenu(User user) {
        while (true) {
            System.out.println("\n--- Member menu---");
            System.out.println("1. Purchase membership");
            System.out.println("0. Logout");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> handlePurchaseMembership(user);
                case "0" -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void trainerMenu(User user) {
        while (true) {
            System.out.println("\n--- Trainer Menu ---");
            System.out.println("1. View My Workout Classes");
            System.out.println("2. Create New Workout Class");
            System.out.println("3. Delete Workout class");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> handleViewWorkoutClasses(user.getUserId());
                case "2" -> handleAddWorkoutClass();
                case "3" -> handleDeleteWorkoutClass();
                case "0" -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void handlePurchaseMembership(User user) {
        System.out.println("Enter membership type (e.g., Monthly Annual): ");
        String type = scanner.nextLine();

        System.out.println("Enter membership description (e.g., Trainer, Normal Member, VIP): ");
        String desc = scanner.nextLine();

        System.out.println("Enter membership cost: ");
        double cost = Double.parseDouble(scanner.nextLine());

        Membership membership = new Membership(0, type, desc, cost, user.getUserId());

        try {
            membershipService.purchaseMembership(membership);
            System.out.println("Membership purchased successfully.");
        } catch (SQLException e) {
            System.out.println("Error purchasing membership: " + e.getMessage());
        }
    }

    private void handleViewWorkoutClasses(int trainerId) {
        try {
            List<WorkoutClass> classes = workoutClassService.getWorkoutClassesByTrainerId(trainerId);
            if (classes.isEmpty()) {
                System.out.println("No workout classes found.");
                return;
            }
            for (WorkoutClass wc : classes) {
                System.out.printf("ID: %d | Type: %s | Desc: %s | Trainer ID: %d\n",
                        wc.getWorkoutClassId(), wc.getWorkoutClassType(),
                        wc.getWorkoutClassDesc(), wc.getTrainerId());
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving workout classes: " + e.getMessage());
        }
    }

    // admin view
    private void adminMenu() {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View All Memberships");
            System.out.println("2. View Total Revenue");
            System.out.println("3. Delete Workout Class");
            System.out.println("0. Logout");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> handleViewMemberships();
                case "2" -> handleViewRevenue();
                case "3" -> handleDeleteWorkoutClass();
                case "0" -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void handleViewMemberships() {
        try {
            List<Membership> memberships = membershipService.listAllMemberships();
            for (Membership m : memberships) {
                System.out.printf("ID: %d | Type: %s | Desc: %s | Cost: %.2f | Member ID: %d\n",
                        m.getMembershipId(), m.getMembershipType(), m.getMembershipDesc(),
                        m.getMembershipCost(), m.getMemberId());
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving memberships: " + e.getMessage());
        }
    }

    private void handleViewRevenue() {
        try {
            double revenue = membershipService.getTotalRevenue();
            System.out.printf("Total Revenue: $%.2f\n", revenue);
        } catch (SQLException e) {
            System.out.println("Error calculating revenue: " + e.getMessage());
        }
    }

    private void handleAddWorkoutClass() {
        try {
            System.out.println("Enter workout class type: ");
            String type = scanner.nextLine();

            System.out.println("Enter description: ");
            String desc = scanner.nextLine();

            System.out.println("Enter trainer ID: ");
            int trainerId = Integer.parseInt(scanner.nextLine());

            WorkoutClass wc = new WorkoutClass(0, type, desc, trainerId);

            workoutClassService.addWorkoutClass(wc);
            System.out.println("Workout class added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding workout class: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid trainer ID.");
        }
    }

    private void handleDeleteWorkoutClass() {
        try {
            System.out.println("Enter the ID of the workout class to delete: ");
            int id = Integer.parseInt(scanner.nextLine());

            workoutClassService.deleteWorkoutClass(id);
            System.out.println("Workout class deleted.");
        } catch (SQLException e) {
            System.out.println("Error deleting workout class: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }


}
