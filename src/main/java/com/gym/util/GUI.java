package com.gym.util;

import com.gym.service.MembershipService;
import com.gym.service.UserService;
import com.gym.service.WorkoutClassService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GUI extends Application {
    private UserService userService;
    private MembershipService membershipService;
    private WorkoutClassService workoutClassService;


    public GUI() {

    }

    public GUI(UserService userService, MembershipService membershipService, WorkoutClassService workoutClassService) {
        this.userService = userService;
        this.membershipService = membershipService;
        this.workoutClassService = workoutClassService;
    }

    public static void main(String[] args) {
        launch(args);
    }

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

        loginButton.setOnAction(e -> showLoginScreen());
        registerButton.setOnAction(e -> showRegisterScreen());
    }

    private void showLoginScreen() {
        System.out.println("Login triggered");
    }

    private void showRegisterScreen() {
        System.out.println("Register triggered");
    }

}
