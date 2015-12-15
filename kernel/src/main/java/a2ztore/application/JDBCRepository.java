package a2ztore.application;

import a2ztore.model.User;
import a2ztore.view.Repository;
import javaslang.control.Try;

import java.sql.*;

import static a2ztore.application.DatabaseConnector.tryConnection;
import static java.lang.String.format;

public class JDBCRepository implements Repository {

    public JDBCRepository() {
        createUsersTable();
    }

    @Override
    public void add(User user) {
        tryConnection()
                .mapTry(this::prepareInsertUserStatement)
                .andThen(setNameOf(user))
                .andThen(setSurnameOf(user))
                .andThen(PreparedStatement::executeUpdate);
    }

    private PreparedStatement prepareInsertUserStatement(Connection connection) {
        return Try.of(() -> connection.prepareStatement(insertUserQuery())).get();
    }

    private String insertUserQuery() {
        return "INSERT INTO Users (username, surname) VALUES (?, ?)";
    }

    private Try.CheckedConsumer<PreparedStatement> setNameOf(User user) {
        return statement -> statement.setString(1, user.name());
    }

    private Try.CheckedConsumer<PreparedStatement> setSurnameOf(User user) {
        return statement -> statement.setString(2, user.surname());
    }

    @Override
    public User get(String username) {
        return tryConnection()
                    .mapTry(Connection::createStatement)
                    .mapTry(statement -> statement.executeQuery(selectUsersWithSame(username)))
                    .andThen(ResultSet::next)
                    .mapTry(this::toUser)
                    .get();
    }

    private String selectUsersWithSame(String username) {
        return format("SELECT * FROM Users WHERE username=\"%s\"", username);
    }

    private User toUser(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getString("username"),
                        resultSet.getString("surname"));
    }

    @Override
    public void update(String oldUsername, String newUsername) {
        String updatePerson = format("UPDATE Users SET username=\"%s\" WHERE username=\"%s\"", newUsername, oldUsername);
        executeQuery(updatePerson);
    }

    @Override
    public void delete(String username) {
        executeQuery(deleteUserWithSame(username));
    }

    private String deleteUserWithSame(String username) {
        return format("DELETE FROM Users WHERE username=\"%s\"", username);
    }

    private void createUsersTable() {
        executeQuery(createTableUsers());
    }

    private String createTableUsers() {
        return "CREATE TABLE Users (user_id int(11) NOT NULL AUTO_INCREMENT," +
                                    "username varchar(45) NOT NULL," +
                                    "surname varchar(45) NOT NULL," +
                                    "PRIMARY KEY (user_id));";
    }

    private Try<Statement> executeQuery(String sql) {
        return tryConnection()
                    .mapTry(Connection::createStatement)
                    .andThen(statement -> statement.executeUpdate(sql));
    }

}
