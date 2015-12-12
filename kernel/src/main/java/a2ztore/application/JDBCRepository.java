package a2ztore.application;

import a2ztore.model.Person;
import a2ztore.view.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static a2ztore.application.DatabaseConnector.stablishConnection;
import static java.lang.String.format;

public class JDBCRepository implements Repository {

    public JDBCRepository() {
        createUsersTable();
    }

    @Override
    public void add(Person person) {
        String sql = "INSERT INTO Users (username, surname) VALUES (?, ?)";
        try (Connection connection = stablishConnection()) {
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, person.name());
                statement.setString(2, person.surname());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Person get(String username) {
        try(Connection connection = stablishConnection()) {
            String sql = format("SELECT * FROM Users WHERE username=\"%s\"", username);
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            resultSet.next();

            return new Person(resultSet.getString("username"),
                    resultSet.getString("surname"));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(String oldUsername, String newUsername) {
        try (Connection connection = stablishConnection()) {
            String sql = format("UPDATE Users SET username=\"%s\" WHERE username=\"%s\"", newUsername, oldUsername);
            if (connection != null) {
                connection.createStatement().executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String username) {
        try(Connection connection = stablishConnection()) {
            String sql = format("DELETE FROM Users WHERE username=\"%s\"", username);
            if (connection != null)
                connection.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createUsersTable() {
        try (Connection connection = stablishConnection()) {
            String sql = "CREATE TABLE Users (user_id int(11) NOT NULL AUTO_INCREMENT," +
                                             "username varchar(45) NOT NULL," +
                                             "surname varchar(45) NOT NULL," +
                                             "PRIMARY KEY (user_id));";
            if (connection != null)
                connection.createStatement().executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}