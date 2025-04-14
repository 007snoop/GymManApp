package com.gym.util;

import com.gym.model.User;
import com.gym.service.MembershipService;
import com.gym.service.UserService;
import com.gym.service.WorkoutClassService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GUI extends Application {
    private UserService userService;
    private MembershipService membershipService;
    private WorkoutClassService workoutClassService;


    /**
     * Overrides init method in javafx before start() runs for db connection
     */
    @Override
    public void init() {
        try {
            Connection connection = DBUtil.getConnection(); // replace with your DB setup
            this.userService = new UserService(connection);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1); // Stop app if DB init fails
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Entry point for app start
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        Label welcomeLabel = new Label("Hello Colin");
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        VBox layout = new VBox(20, welcomeLabel, loginButton, registerButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene mainScene = new Scene(layout,400, 300);
        primaryStage.setTitle("Gym Management System");
        primaryStage.setScene(mainScene);
        primaryStage.show();

        loginButton.setOnAction(event -> showLoginScreen(primaryStage));
        registerButton.setOnAction(event -> showRegisterScreen());
    }


    /**
     * Starts login screen
     *
     * @param primaryStage Main app scene
     */
    private void showLoginScreen(Stage primaryStage) {
        Label titleLabel = new Label("Login");
        Label usernameLabel = new Label("Username: ");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Label feedbackLabel = new Label();

        Button loginButton = new Button("Login");
        Button backButton = new Button("Back");

        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.getChildren().addAll(
                titleLabel,
                usernameLabel, usernameField,
                passwordLabel, passwordField,
                loginButton, backButton,
                feedbackLabel
        );

        Scene loginScene = new Scene(loginLayout, 400, 300);

        primaryStage.setScene(loginScene);

        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            try {
                User user = userService.getUserByUsername(username);
                if (user != null) {
                    boolean valid = userService.verifyPass(password, user.getPassword());
                    if (valid) {
                        feedbackLabel.setText("Login successful!");
                        // add scene method
                        switch (user.getRole().toLowerCase()) {
                            case "admin" -> showAdminDash(primaryStage, user);
                            case "trainer" -> showTrainerDash(primaryStage, user);
                            case "member" -> showMemberDash(primaryStage, user);
                            default -> feedbackLabel.setText("Unknown role@: " + user.getRole());
                        }
                    } else {
                        feedbackLabel.setText("Invalid user or pass.");
                    }
                } else {
                    feedbackLabel.setText("Invalid user or pass.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                feedbackLabel.setText("DB ERROR!");
            }
        });

        backButton.setOnAction(event -> start(primaryStage));

    }

    /**
     * Starts admin dashboard
     *
     * @param stage changes primaryStage to new stage
     * @param user uses role based user selection for "admin"
     */
    private void showAdminDash(Stage stage, User user) {
        Label welcomeLabel = new Label("Welcome, Admin " + user.getUsername());

        Button viewUsersButton = new Button("View All Users");
        Button viewClassesButton = new Button("View All Classes");
        Button deleteUserButton = new Button("Delete a User");
        Button logoutButton = new Button("Logout");
    
        viewUsersButton.setOnAction(event -> {
            // TODO: Fetch and display user list
            try {
                List<User> users = userService.getAllUsers();

                VBox userListLayout = new VBox(10);
                userListLayout.setPadding(new Insets(10));
                userListLayout.setAlignment(Pos.TOP_LEFT);

                Label titleLabel = new Label("All Registered Users");
                // css styling
                titleLabel.setStyle("""
                        -fx-font-weight: bold;
                        -fx-font-size: 16px;""");

                userListLayout.getChildren().add(titleLabel);

                for (User u : users) {
                    Label userLabel = new Label("ID: " + u.getUserId() + " | Username: " + u.getUsername() + " | " +
                            "Role: " + u.getRole());
                    userListLayout.getChildren().add(userLabel);
                }

                Button backButton = new Button("back");
                backButton.setOnAction(event1 -> showAdminDash(stage, user));
                userListLayout.getChildren().add(backButton);


                Scene userScene = new Scene(userListLayout, 400, 300);
                stage.setScene(userScene);


            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        
        viewClassesButton.setOnAction(event -> {
            // TODO: Fetch and display class list
        });

        deleteUserButton.setOnAction(event -> {
            // TODO: Prompt to enter username for deletion
        });
        logoutButton.setOnAction(event -> start(stage));

        VBox layout = new VBox(10, welcomeLabel, viewUsersButton, viewClassesButton, deleteUserButton, logoutButton);

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 400, 300));
    }

    /**
     * starts trainer dashboard
     *
     * @param stage changes primaryStage to new stage
     * @param user uses role based user selection for "trainer"
     */
    private void showTrainerDash(Stage stage, User user) {
        Label welocomeLabel = new Label("Welcome, Trainer " + user.getUsername());

        Button viewYourClasses = new Button("View your Classes");
        Button createWorkoutClass = new Button("Create a Workout Class");
        Button deleteWorkoutClass = new Button("Delete a Workout Class");

        Button logoutButton = new Button("Logout");

        viewYourClasses.setOnAction(event -> {
            // TODO: Fetch and display trainer classes based on trainer_id
        });

        createWorkoutClass.setOnAction(event -> {
            //TODO: Add workout class to database
        });

        deleteWorkoutClass.setOnAction(event -> {
            //TODO: Remove a workout class from database
        });

        logoutButton.setOnAction(event -> start(stage));

        VBox layout = new VBox(10, welocomeLabel, logoutButton);

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 400, 300));
    }

    /**
     * Starts member dashboard
     *
     * @param stage changes primaryStage to new stage
     * @param user uses role based user selection for "member"
     */
    private void showMemberDash(Stage stage, User user) {
        Label welcomeLabel = new Label("Welcome, Member " + user.getUsername());

        Button viewAvailableClasses = new Button("View Available Workout Classes");
        Button joinWorkoutClass = new Button("Join Available Workout Class");
        Button viewEnrolledClasses = new Button("View Your Enrolled classes");

        Button logoutButton = new Button("Logout");

        viewAvailableClasses.setOnAction(event -> {
            // TODO: Fetch and display all available Classes
        });

        joinWorkoutClass.setOnAction(event -> {
            // TODO: Add method to attach user to workout class
        });

        viewEnrolledClasses.setOnAction(event -> {
            // TODO: Fetch and display Enrolled classes based on member_id attached to workout class
        });

        logoutButton.setOnAction(event -> start(stage));

        VBox layout = new VBox(10, welcomeLabel, logoutButton);

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 400, 300));
    }

    private void showRegisterScreen() {
        System.out.println("Register triggered");
    }

}
