package com.gym.model;

public class Member extends User {
    public Member(int userId, String username, String password, String email,
                  String phoneNumber, String address) {
        super(userId, username, password, email, phoneNumber, address);
    }

    public Member(int userId, String username, String password) {
        super(userId, username, password);
    }

    @Override
    public String getRole() {
        return "Member";
    }
}
