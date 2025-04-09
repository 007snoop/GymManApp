package com.gym.dao;

import com.gym.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    // register a new user
    public void registerUser(User user) throws SQLException {
        String SQL = """
                INSERT INTO users (
                username,
                password,
                email,
                phone_number,
                address,
                role
                ) VALUES (?,?,?,?,?,?)""";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getRole());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // get user by username
    public User getUserByUsername(String username) throws SQLException {
        String SQL = """
                SELECT * FROM
                users
                WHERE
                username = ?;""";
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getString("role")
                );
            }
        }
        return null;


    }
}
