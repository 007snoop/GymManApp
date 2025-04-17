# Project Documentation – Gym Management System

***

## `model/` – Data Models

These classes represent the core entities in your system.

- `User` 
  - Super class for all user roles. Contains common fields like id, username, password, email, phone, address.

- `Admin`
  - Inherits from User. Represents users with administrative privileges.

- `Trainer`
  - Inherits from User. Trainers can manage workout classes and members.

- `Member`
  - Inherits from User. Represents gym members who can join classes.

- `WorkoutClass`
  - Represents a workout class. Includes fields like id, name, schedule, and trainerId.

- `Membership`
  - Represents a member’s enrollment in a class. Links Member and WorkoutClass.

## `dao/` – Data Access Objects

Provides direct database interaction using SQL queries.

- `UserDAO`
  - Handles all user-related database operations: register, login, retrieve user by role or ID.

- `WorkoutClassDAO`
  - Manages workout class records: creation, updates, deletes, and fetching class info.

- `MembershipDAO`
  - Handles member-class relationships, including registration and removal from classes.

- `MemberWorkoutClassDAO`
  - Combines logic for fetching classes joined by a specific member.

## `service/` – Business Logic

Contains logic between the DAO and the GUI/console layers.

- `UserService`
  - Business logic for user management: registration, authentication, role-based routing.
  Handles exceptions like DupUserEx.

- `WorkoutClassService`
  - Manages workout class logic (e.g., fetching by trainer, availability).

- `MembershipService`
  - Manages joining and viewing memberships from an admin/trainer perspective.

- `MemberWorkoutClassService`
  - Provides functions to view or interact with a member’s enrolled classes.

## `util/` – Utilities and UI Components

Helpers, exception classes, and UI logic.

- `DBUtil`
  - Manages connection to the PostgresSQL database.

- `DBSetup`
  - Responsible for setting up and seeding the database schema if not already present.

- `DupUserEx`
  - Custom exception for handling duplicate user registration attempts.

- `ConsoleUI`~Optional~
  - (If used) Provides a basic console-based interface for interacting with the system. 

- `GUI`
  - JavaFX-powered graphical interface with login and registration screens. Redirects based on role. Contains logic for 
  alert dialogs and screen transitions.
***
## Database Schema

The Database is handled through `DBSetup.java` to build these schemas;

```aiignore

CREATE TABLE IF NOT EXISTS users (
                    user_id SERIAL PRIMARY KEY,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    phone_number VARCHAR(15),
                    address TEXT,
                    role TEXT NOT NULL CHECK (role IN ('Admin', 'Trainer', 'Member'))
                    );
                    
CREATE TABLE IF NOT EXISTS memberships (
                    membership_id SERIAL PRIMARY KEY,
                    membership_type TEXT NOT NULL,
                    membership_desc TEXT,
                    membership_cost NUMERIC(10,2) NOT NULL,
                    member_id INTEGER REFERENCES users(user_id)
                    );
                    
CREATE TABLE IF NOT EXISTS workout_classes (
                    workout_class_id SERIAL PRIMARY KEY,
                    workout_class_type TEXT NOT NULL,
                    workout_class_desc TEXT,
                    trainer_id INTEGER REFERENCES users(user_id)
                    );
                    
CREATE TABLE IF NOT EXISTS member_workout_classes (
                    member_id INT REFERENCES users(user_id),
                    workout_class_id INT REFERENCES workout_classes(workout_class_id) ON DELETE CASCADE,
                    PRIMARY KEY (member_id, workout_class_id)
                    );
```
***

## Batch File Documentation

| Step                           | Description                                                              |
|--------------------------------|--------------------------------------------------------------------------|
| Check for Maven                | Uses the `where` command to verify Maven is installed and available.     |
| Clean and install dependencies | Runs `mvn clean install` to build project and resolve dependency issues. |
| Run JavaFX app                 | Launches the app using `mvn javafx:run` to pass the javafx in properly.  |
| Exit Handling                  | Displays errors and stays open to keep DB connection alive.              |
