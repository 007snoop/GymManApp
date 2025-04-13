package com.gym.util;

import com.gym.service.MembershipService;
import com.gym.service.UserService;
import com.gym.service.WorkoutClassService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
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
        // main layout

        // ui elements
        Label label = new Label("Hello Colin");


        Scene scene = new Scene(new StackPane(label),400, 300);
        primaryStage.setTitle("Gym Management System");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    private void showLoginScreen() {
        System.out.println("Login triggered");
    }

    private void showRegisterScreen() {
        System.out.println("Register triggered");
    }

}
