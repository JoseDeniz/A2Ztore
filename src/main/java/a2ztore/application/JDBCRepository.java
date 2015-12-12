package a2ztore.application;

import a2ztore.model.Person;
import a2ztore.view.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static a2ztore.application.DatabaseConnector.stablishConnection;

public class JDBCRepository implements Repository {

    public JDBCRepository() {
        createUsersTable();
    }

    @Override
    public void add(Person person) {
        String sql = "INSERT INTO Users (username, fullname) VALUES (?, ?)";
        try (Connection connection = stablishConnection()) {
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
        try (Connection connection = stablishConnection()) {
            String sql = "CREATE TABLE Users (user_id int(11) NOT NULL AUTO_INCREMENT," +
                                             "username varchar(45) NOT NULL," +
                                             "fullname varchar(45) NOT NULL," +
                                             "PRIMARY KEY (user_id));";
            if (connection != null)
                connection.createStatement().executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
