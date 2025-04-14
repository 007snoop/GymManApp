package com.gym.model;

public class Admin extends User {
    public Admin(int userId, String username, String password, String email, String phoneNumber, String address) {
        super(userId, username, password, email, phoneNumber, address);
    }

    public Admin(int id, String username, String password) {
        super(id, username, password);
    }
    @Override
    public String getRole() {
        return "Admin";
    }
}
