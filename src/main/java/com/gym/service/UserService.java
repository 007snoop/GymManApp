package com.gym.service;

import com.gym.dao.UserDAO;
import com.gym.model.Admin;
import com.gym.model.Member;
import com.gym.model.Trainer;
import com.gym.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static com.gym.util.DBUtil.connection;

public class UserService {
    private final UserDAO userDAO;

    public UserService(Connection connection) {
        this.userDAO = new UserDAO(connection);
    }
    // make user with hashed pass
    public void register(User user) throws SQLException {
        String hashedPass = hashPassword(user.getPassword());
        user.setPassword(hashedPass);
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

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }


}
