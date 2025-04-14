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
        Label label = new Label("Hello Colin");
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        VBox layout = new VBox(20, label, loginButton, registerButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene mainScene = new Scene(layout,400, 300);
        primaryStage.setTitle("Gym Management System");
        primaryStage.setScene(mainScene);
        primaryStage.show();

        loginButton.setOnAction(e -> showLoginScreen(primaryStage));
        registerButton.setOnAction(e -> showRegisterScreen());
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

        loginButton.setOnAction(e -> {
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

        backButton.setOnAction(e -> start(primaryStage));

    }

    /**
     * Starts admin dashboard
     *
     * @param stage changes primaryStage to new stage
     * @param user uses role based user selection for "admin"
     */
    private void showAdminDash(Stage stage, User user) {
        Label label = new Label("Welcome, Admin " + user.getUsername());
        Button logoutButton = new Button("Logout");

        logoutButton.setOnAction(e -> start(stage));

        VBox layout = new VBox(10, label, logoutButton);

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
        Label label = new Label("Welcome, Trainer " + user.getUsername());
        Button logoutButton = new Button("Logout");

        logoutButton.setOnAction(e -> start(stage));

        VBox layout = new VBox(10, label, logoutButton);

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
        Label label = new Label("Welcome, Member " + user.getUsername());
        Button logoutButton = new Button("Logout");

        logoutButton.setOnAction(e -> start(stage));

        VBox layout = new VBox(10, label, logoutButton);

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 400, 300));
    }

    private void showRegisterScreen() {
        System.out.println("Register triggered");
    }

}
