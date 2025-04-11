import com.gym.util.ConsoleUI;
import com.gym.util.DBSetup;
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
            DBSetup.initDB(connection);
            ConsoleUI consoleUI = new ConsoleUI(connection);
            consoleUI.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
