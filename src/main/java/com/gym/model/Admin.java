package com.gym.model;

public class Admin extends User {
    public Admin(int userId, String username, String password, String email, String phoneNumber, String address) {
        super(userId, username, password, email, phoneNumber, address);
    }

    @Override
    public String getRole() {
        return "Admin";
    }
}
