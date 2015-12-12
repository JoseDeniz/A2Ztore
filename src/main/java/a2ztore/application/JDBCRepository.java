package a2ztore.application;

import a2ztore.model.Person;
import a2ztore.view.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class JDBCRepository implements Repository {

    public static final String DB_URL = "jdbc:mysql://localhost:3306/a2ztoreDB";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "secret";

    public JDBCRepository() {
        createUsersTable();
    }

    @Override
    public void add(Person person) {
        String sql = "INSERT INTO Users (username, fullname) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, person.name());
                statement.setString(2, person.fullname());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createUsersTable() {
        try (Connection connection = getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "CREATE TABLE Users (user_id int(11) NOT NULL AUTO_INCREMENT," +
                                             "username varchar(45) NOT NULL," +
                                             "fullname varchar(45) NOT NULL," +
                                             "PRIMARY KEY (user_id));";
            connection.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
