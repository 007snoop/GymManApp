package com.gym.model;

public class Trainer extends User {

    public Trainer(int userId, String username, String password, String email, String phoneNumber, String address) {
        super(userId, username, password, email, phoneNumber, address);
    }

    public Trainer(int userId, String username, String password) {
        super(userId, username, password);
    }

    @Override
    public String getRole() {
        return "Trainer";
    }
}
