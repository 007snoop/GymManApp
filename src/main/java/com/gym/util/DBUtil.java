package com.gym.util;


import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    public static Connection connection;

    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }

        try {
            Dotenv dotenv = Dotenv.load();

            String url = dotenv.get("DB_URL");
            String username = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");

            assert url != null;
            connection = DriverManager.getConnection(url, username, password);
            DBSetup.initDB(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}
