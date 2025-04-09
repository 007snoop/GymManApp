package com.gym;

import com.gym.util.DBUtil;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = DBUtil.getConnection();
            if (connection != null ) {
                System.out.println("Connected to PSQL");
            } else {
                System.out.println("PSQL CONNECTION ERROR");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
