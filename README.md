# The Coding Crew Gym Management App

A Java-based application for managing gym users and workout classes. This project uses PostgreSQL for data persistence, JavaFX for the graphical user interface, and Maven for build management.

## Features

- Role-based access control for Admin, Trainer, and Member accounts
- Admin Dashboard:
    - View all users
    - Delete users
- Trainer Dashboard:
    - Create new workout classes
    - View and delete workout classes they created
- Member Dashboard:
    - View all available workout classes
    - Enroll in workout classes
    - View enrolled classes
- PostgreSQL database integration
- JavaFX GUI interface

## Prerequisites

- Java 17 or later
- Maven
- PostgreSQL server

## Environment Configuration

Create a `.env` file in the root directory of the project with your PostgreSQL database details.

Example `.env` file:

```
DB_URL=jdbc:postgresql://localhost:5432/<gymmanapp>
DB_USER=yourusername 
DB_PASSWORD=yourpassword
```

If using IntelliJ IDEA, you can add these values under **Run > Edit Configurations > Environment variables**.

## Installation and Running

Clone the repository:
```
git clone https://github.com/007snoop/GymManApp.git
cd GymManApp
```

Make sure your PostgreSQL database is running and configured properly.

Run the application:

### On Windows

```
run.bat
```

### On Linux/MacOS

```
chmod +x run.sh
./run.sh
```

### Or via maven

```
mvn clean compile javafx:run
```


## Test Accounts

The following accounts can be used to test the system:

| Role    | Username  | Password     |
|---------|-----------|--------------|
| Admin   | admin1    | adminpass    |
| Trainer | trainer1  | trainerpass  |
| Member  | member1   | memberpass   |

Additional test users may exist depending on your current database seed data.

## Technologies Used

- Java 17
- JavaFX
- Maven
- PostgreSQL
- dotenv-java (or manual environment variable loader)

## Authors

- [007snoop](https://github.com/007snoop)
- [CodyC1998](https://github.com/CodyC1998)
- [JaowadH](https://github.com/JaowadH)

