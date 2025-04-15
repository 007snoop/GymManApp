package com.gym.util;

import java.sql.Connection;
import java.sql.Statement;

public class DBSetup {


    public static void initDB(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            // create user tables if not exists
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users (
                    user_id SERIAL PRIMARY KEY,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    phone_number VARCHAR(15),
                    address TEXT,
                    role TEXT NOT NULL CHECK (role IN ('Admin', 'Trainer', 'Member'))
                    )""");
            // create membership tables if not exists
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS memberships (
                    membership_id SERIAL PRIMARY KEY,
                    membership_type TEXT NOT NULL,
                    membership_desc TEXT,
                    membership_cost NUMERIC(10,2) NOT NULL,
                    member_id INTEGER REFERENCES users(user_id)
                    )""");
            // create workout table if not exists
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS workout_classes (
                    workout_class_id SERIAL PRIMARY KEY,
                    workout_class_type TEXT NOT NULL,
                    workout_class_desc TEXT,
                    trainer_id INTEGER REFERENCES users(user_id)
                    )""");
            // create member work out classes table if not exists
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS member_workout_classes (
                    member_id INT REFERENCES users(user_id),
                    workout_class_id INT REFERENCES workout_classes(workout_class_id),
                    PRIMARY KEY (member_id, workout_class_id)
                    )""");

            // print check table log
            System.out.println("DB tables created/checked successfully");
        } catch (Exception e) {
            System.err.println("DB CREATION/CHECK ERROR" + e.getMessage());
            e.printStackTrace();
        }
    }
}
