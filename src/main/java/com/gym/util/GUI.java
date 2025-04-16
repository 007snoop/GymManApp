package com.gym.util;

import com.gym.model.*;
import com.gym.service.MemberWorkoutClassService;
import com.gym.service.MembershipService;
import com.gym.service.UserService;
import com.gym.service.WorkoutClassService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class GUI extends Application {
    private UserService userService;
    private MembershipService membershipService;
    private MemberWorkoutClassService memberWorkoutClassService;
    private WorkoutClassService workoutClassService;
    /**
     * Overrides init method in javafx before start() runs for db connection
     */
    @Override
    public void init() {
        try {
            Connection connection = DBUtil.getConnection(); // replace with your DB setup
            this.userService = new UserService(connection);
            this.workoutClassService = new WorkoutClassService(connection);
            this.memberWorkoutClassService = new MemberWorkoutClassService(connection);
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
        Label welcomeLabel = new Label("Welcome to the Gym Management System");
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
        registerButton.setOnAction(event -> showRegisterScreen(primaryStage));
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
            try {
                List<WorkoutClass> classList = workoutClassService.getAllWorkoutClasses();

                VBox classContainer = new VBox(10);
                classContainer.setPadding(new Insets(10));

                if (classList.isEmpty()) {
                    Label emptyLabel = new Label("No workout classes found.");
                    classContainer.getChildren().add(emptyLabel);
                } else {
                    for (WorkoutClass wc : classList) {
                        VBox card = new VBox(5);
                        card.setPadding(new Insets(10));

                        card.setStyle("""
                                -fx-background-color: #e0f7fa;
                                -fx-border-color: #00838f;
                                -fx-border-radius: 5;
                                -fx-background-radius: 5;
                                """);

                        Label idLabel = new Label("ClassID: " + wc.getWorkoutClassId());
                        Label typeLabel = new Label("Type: " + wc.getWorkoutClassType());
                        Label descLabel = new Label("Description: " + wc.getWorkoutClassDesc());
                        Label trainerLabel = new Label("Trainer ID: " + wc.getTrainerId());

                        HBox buttonRow = new HBox(10);
                        Button deleteButton = getDeleteButton(wc, classContainer, card);
                        buttonRow.getChildren().addAll(deleteButton);
                        card.getChildren().addAll(idLabel, typeLabel, descLabel, trainerLabel, buttonRow);
                        classContainer.getChildren().add(card);
                    }
                }
                ScrollPane scrollPane = new ScrollPane(classContainer);
                scrollPane.setFitToWidth(true);

                Button backButton = new Button("Back");
                VBox layout = new VBox(10, scrollPane,backButton);
                layout.setPadding(new Insets(10));
                layout.setAlignment(Pos.CENTER);

                Stage viewStage = new Stage();
                viewStage.setTitle("All Workout Classes");
                viewStage.setScene(new Scene(layout, 400, 300));
                backButton.setOnAction(event1 -> viewStage.close());
                viewStage.show();

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "ERROR", "Issue loading workout classes.");
            }
        });

        deleteUserButton.setOnAction(event -> {

            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("Delete User");
            dialog.setHeaderText("Delete a User");
            dialog.setContentText("Enter username to delete:");

            dialog.showAndWait().ifPresent(username -> {
                try {
                    boolean deleted = userService.deleteUser(username);
                    Alert alert;
                    if (deleted) {
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("User Deleted");
                        alert.setHeaderText(null);
                        alert.setContentText("User: '" + username + "' deleted successfully.");
                    } else {
                        alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Deletion failed");
                        alert.setHeaderText(null);
                        alert.setContentText("User '" + username + "'not found or could not be deleted.");
                    }

                    alert.showAndWait();
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
                    alert.showAndWait();
                }
            });
        });


        logoutButton.setOnAction(event -> start(stage));

        VBox layout = new VBox(10, welcomeLabel, viewUsersButton, viewClassesButton, deleteUserButton, logoutButton);

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 400, 300));
    }

    @NotNull
    private Button getDeleteButton(WorkoutClass wc, VBox classContainer, VBox card) {
        Button deleteButton = new Button("Delete");

        // TODO: add edit button

        deleteButton.setOnAction(event1 -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Deletion");
            confirm.setHeaderText(null);
            confirm.setContentText("Are you sure you want to delete class ID: " + wc.getWorkoutClassId() + "?");

            Optional<ButtonType> result = confirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    workoutClassService.deleteWorkoutClass(wc.getWorkoutClassId());
                    showAlert(Alert.AlertType.INFORMATION, "Deleted", "Class successfully deleted.");
                    classContainer.getChildren().remove(card);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "ERROR", "Could not delete class.");
                }
            }
        });
        return deleteButton;
    }

    /**
     * starts trainer dashboard
     *
     * @param stage changes primaryStage to new stage
     * @param user uses role based user selection for "trainer"
     */
    private void showTrainerDash(Stage stage, User user) {
        Label welcomeLabel = new Label("Welcome, Trainer " + user.getUsername());

        Button viewYourClasses = new Button("View your Classes");
        Button createWorkoutClass = new Button("Create a Workout Class");
        Button deleteWorkoutClass = new Button("Delete a Workout Class");
        Button logoutButton = new Button("Logout");


        viewYourClasses.setOnAction(event -> {
            try {
                List<WorkoutClass> classes = workoutClassService.getWorkoutClassesByTrainerId(user.getUserId());

                if (classes.isEmpty()) {
                    showAlert(Alert.AlertType.INFORMATION, "No Classes", "You have no classes assigned.");
                    return;
                }

                StringBuilder classList = new StringBuilder("Your Classes:\n");
                for (WorkoutClass wc : classes) {
                    classList.append("ID: ").append(wc.getWorkoutClassId())
                            .append(" | Type: ").append(wc.getWorkoutClassType())
                            .append(" | Desc: ").append(wc.getWorkoutClassDesc())
                            .append("\n");
                }

                showAlert(Alert.AlertType.INFORMATION, "Your Classes", classList.toString());

            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Could not retrieve classes.\n" + e.getMessage());
            }
        });

        createWorkoutClass.setOnAction(event -> {
            //TODO: Add workout class to database
            TextInputDialog typeDialog = new TextInputDialog();

            typeDialog.setTitle("Create Class");
            typeDialog.setHeaderText("Enter class type:");
            typeDialog.showAndWait().ifPresent(type -> {
                TextInputDialog descDialog = new TextInputDialog();

                descDialog.setTitle("Class Description");
                descDialog.setHeaderText("Enter class description:");
                descDialog.showAndWait().ifPresent(desc -> {

                    try {
                        WorkoutClass newClass = new WorkoutClass(type, desc, user.getUserId());
                        workoutClassService.addWorkoutClass(newClass);
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Workout class created.");
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to create class.\n" + e.getMessage());
                    }
                });
            });
        });

        deleteWorkoutClass.setOnAction(event -> {
            //TODO: Remove a workout class from database
            TextInputDialog deleteDialog = new TextInputDialog();
            deleteDialog.setTitle("Delete Workout Class");
            deleteDialog.setHeaderText("Enter the ID of the class to delete:");
            deleteDialog.showAndWait().ifPresent(idStr -> {
                try {
                    int id = Integer.parseInt(idStr);
                    workoutClassService.deleteWorkoutClass(id);
                    showAlert(Alert.AlertType.INFORMATION, "Deleted", "Workout class deleted.");
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid numeric ID.");
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete class.\n" + e.getMessage());
                }
            });
        });

        logoutButton.setOnAction(event -> start(stage));

        VBox layout = new VBox(10, welcomeLabel, viewYourClasses, createWorkoutClass, deleteWorkoutClass, logoutButton);

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
            try {
                List<WorkoutClass> classes = workoutClassService.getAllWorkoutClasses();

                VBox classListLayout = new VBox(10);
                classListLayout.setPadding(new Insets(10));


                for (WorkoutClass wc : classes) {
                    String classInfo = String.format("ID: %d\nType: %s\nDescription: %s\nTrainer ID: %d",
                            wc.getWorkoutClassId(), wc.getWorkoutClassType(), wc.getWorkoutClassDesc(), wc.getTrainerId());

                    Label classInfoLabel = new Label(classInfo);

                    classInfoLabel.setWrapText(true);
                    classInfoLabel.setStyle("""
                            -fx-border-color: black;
                            -fx-padding: 10;""");
                    classListLayout.getChildren().add(classInfoLabel);
                }

                Button backButton = new Button("Back");
                backButton.setOnAction(event1 -> showMemberDash(stage, user));

                ScrollPane scrollPane = new ScrollPane(classListLayout);
                scrollPane.setFitToWidth(true);

                VBox layout = new VBox(10, new Label("Available Workout Classes: "), scrollPane, backButton);
                layout.setPadding(new Insets(10));
                layout.setAlignment(Pos.CENTER);

                Scene scene = new Scene(layout, 400, 300);
                stage.setScene(scene);

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error: ", e.getMessage());
            }
        });

        joinWorkoutClass.setOnAction(event -> {
            // TODO: Add method to attach user to workout class
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Join Workout Class");
            dialog.setHeaderText("Join a Workout Class");
            dialog.setContentText("Enter the ID of the workout class you want to join: ");

            dialog.showAndWait().ifPresent(input -> {
                try {
                    int classId = Integer.parseInt(input.trim());
                    memberWorkoutClassService.enroll(user.getUserId(), classId);
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Successfully joined workout class ID: " + classId);
                    successAlert.showAndWait();
                } catch (SQLException | NumberFormatException e) {
                    e.printStackTrace();
                }
            });

        });

        viewEnrolledClasses.setOnAction(event -> {
            // TODO: Fetch and display Enrolled classes based on member_id attached to workout class
           try {
               List<WorkoutClass> enrolledClasses = memberWorkoutClassService.getEnrolledClassesByMemberId(user.getUserId());

               VBox classContainer = new VBox(10);
               classContainer.setPadding(new Insets(10));

               if (enrolledClasses.isEmpty()) {
                   Label noClassLabel = new Label("You are not currently enrolled in any classes.");
                   classContainer.getChildren().add(noClassLabel);
               } else {
                   for (WorkoutClass wc : enrolledClasses) {
                       VBox card = new VBox(10);

                       card.setPadding(new Insets(10));
                       card.setStyle("""
                               -fx-background-color: #f4f4f4;
                               -fx-border-color: #cccccc;
                               -fx-border-radius: 5;
                               -fx-background-radius: 5;
                               """);

                       Label typeLabel = new Label("Type: " + wc.getWorkoutClassType());
                       Label descLabel = new Label("Description: " + wc.getWorkoutClassDesc());
                       Label trainerLabel = new Label("Trainer ID: " + wc.getTrainerId());

                       card.getChildren().addAll(typeLabel, descLabel, trainerLabel);
                       classContainer.getChildren().add(card);
                   }
               }

               ScrollPane scrollPane = new ScrollPane(classContainer);
               scrollPane.setFitToWidth(true);
               scrollPane.setFitToHeight(true);

               Button backButton = new Button("Back");
               VBox layout = new VBox(10, scrollPane, backButton);
               layout.setPadding(new Insets(10));
               layout.setAlignment(Pos.CENTER);


               Stage enrolledStage = new Stage();
               enrolledStage.setTitle("Your Enrolled Classes");
               enrolledStage.setScene(new Scene(layout, 300, 200));
               enrolledStage.show();

               backButton.setOnAction(event1 -> enrolledStage.close());


           } catch (SQLException e) {
               e.printStackTrace();
               showAlert(Alert.AlertType.ERROR, "Error", "Could not retrieve your enrolled classes.");
           }
        });

        logoutButton.setOnAction(event -> start(stage));

        VBox layout = new VBox(10, welcomeLabel, viewAvailableClasses, joinWorkoutClass, viewEnrolledClasses,
                logoutButton);

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 400, 300));
    }
    /**
     * Starts register screen
     *
     * @param primaryStage registration scene
     */
    private void showRegisterScreen(Stage primaryStage) {
        Label titleLabel = new Label("Register New User");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");

        TextField addressField = new TextField();
        addressField.setPromptText("Address");

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("Admin", "Trainer", "Member");
        roleBox.setPromptText("Select Role");

        Label messageLabel = new Label();

        Button registerButton = new Button("Register");
        Button backButton = new Button("Back");

        registerButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();
            String role = roleBox.getValue();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || role == null) {
                messageLabel.setText("All fields are required.");
                return;
            }

            User newUser = switch (role.toLowerCase()) {
                case "admin" ->
                        new Admin(0, username, password, email, phone, address);
                case "trainer" ->
                        new Trainer(0, username, password, email, phone, address);
                case "member" ->
                        new Member(0, username, password, email, phone, address);
                default -> null;
            };

            if (newUser == null) {
                showAlert(Alert.AlertType.ERROR, "Invalid Role", "Please select a valid user role.");
                return;
            }


            try {
                userService.register(newUser);
                showAlert(Alert.AlertType.INFORMATION, "Success", "User registered successfully!");
            } catch (DupUserEx e) {
                showAlert(Alert.AlertType.WARNING, "Registration Error", e.getMessage());
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Something went wrong with the database.");
                e.printStackTrace();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Unexpected Error", e.getMessage());
                e.printStackTrace();
            }
        });

        backButton.setOnAction(e -> start(primaryStage));

        VBox layout = new VBox(10, titleLabel, usernameField, passwordField, emailField, phoneField, addressField, roleBox, registerButton, backButton, messageLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        primaryStage.setScene(new Scene(layout, 450, 500));
    }
}
