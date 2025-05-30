package com.gym.dao;

import com.gym.model.Admin;
import com.gym.model.Member;
import com.gym.model.Trainer;
import com.gym.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.gym.util.DBUtil.connection;

public class UserDAO {
    private final Connection connection;

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
            throw e;
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
                        String role = rs.getString("role").toLowerCase();
                        int userId = rs.getInt("user_id");
                        String uname = rs.getString("username");
                        String password = rs.getString("password");
                        String email = rs.getString("email");
                        String phoneNumber = rs.getString("phone_number");
                        String address = rs.getString("address");

                        return switch (role) {
                            case "admin" -> new Admin(userId, uname, password, email, phoneNumber, address);
                            case "trainer" -> new Trainer(userId, uname, password, email, password, address);
                            case "member" -> new Member(userId, uname, password, email, phoneNumber, address);
                            default -> throw new IllegalArgumentException();
                        };
            }
        }
        return null;


    }
    // get all users for admin
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();

        String SQL = """
                SELECT *
                FROM users""";

        try (PreparedStatement stmt = connection.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");

                User user;
                switch (role.toLowerCase()) {
                    case "admin" -> user = new Admin(id, username, password);
                    case "trainer" -> user = new Trainer(id, username, password);
                    case "member" -> user = new Member(id, username, password);
                    default -> throw new SQLException("Unknown role: " + role);
                }
                users.add(user);
            }
        }
        return users;
    }
    // delete user with admin
    public boolean deleteUser(String username) throws SQLException {
        String SQL = """
                DELETE FROM
                users
                WHERE
                username = ?
                """;

        int affectedRow = 0;
        try (PreparedStatement stmt = connection.prepareStatement(SQL)) {
            stmt.setString(1, username);
            affectedRow = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectedRow > 0;
    }
}
