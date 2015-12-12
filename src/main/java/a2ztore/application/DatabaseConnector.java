package a2ztore.application;

import java.sql.Connection;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class DatabaseConnector {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/a2ztoreDB";
    private static final String USER = "root";
    private static final String PASSWORD = "secret";

    public static Connection stablishConnection() {
        try {
            return getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
