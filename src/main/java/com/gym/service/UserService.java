package com.gym.service;

import com.gym.dao.UserDAO;
import com.gym.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;

public class UserService {
    private UserDAO userDAO;

    public UserService(Connection connection) {
        this.userDAO = new UserDAO(connection);
    }
    // make user with hashed pass
    public void register(User user) throws SQLException {
        String hashedPass = hashPassword(user.getPassword());
        user = new User(user.getUserId(), user.getUsername(), hashedPass, user.getEmail(), user.getPhoneNumber(),
                user.getAddress(), user.getRole());
        userDAO.registerUser(user);
    }

    // hash the pass
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // verify pass on login
    public boolean verifyPass(String inputPassword, String storedHashedPassword) {
        return BCrypt.checkpw(inputPassword, storedHashedPassword);
    }

    // get user by username
    public User getUserByUsername(String username) throws SQLException {
        return userDAO.getUserByUsername(username);
    }

}
