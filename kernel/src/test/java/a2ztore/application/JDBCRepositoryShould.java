package a2ztore.application;

import a2ztore.model.User;
import a2ztore.view.Repository;
import javaslang.control.Try;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static a2ztore.application.DatabaseConnector.tryConnection;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class JDBCRepositoryShould {

    /**
     * TODO List:
     *
     * - User must have name, surname, password, list of bought products
     * - DB must update any field in any table
     *
     * - [Refactor] Implement a Try<T> class to handle with exceptions and try / catch blocks
     */

    private User user;
    private Repository repository;

    @Before
    public void setUp() {
        user = new User("Phillip", "Fry");
        repository = new JDBCRepository();
    }

    @Test
    public void insert_a_person_in_user_table() throws SQLException {
        repository.add(user);
        ResultSet resultSet = executeSelectStatement(user.name());

        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getString("username")).isEqualTo(user.name());
        assertThat(resultSet.getString("surname")).isEqualTo(user.surname());
    }

    @Test
    public void get_a_person_in_user_table() throws SQLException {
        repository.add(user);
        User databaseUser = repository.get(user.name());

        assertThat(databaseUser.name()).isEqualTo(user.name());
        assertThat(databaseUser.surname()).isEqualTo(user.surname());
    }

    @Test
    public void update_the_name_of_the_user() throws SQLException {
        repository.add(user);
        String newUsername = "Phillip J";
        repository.update(user.name(), newUsername);
        ResultSet resultSet = executeSelectStatement(newUsername);

        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getString("username")).isEqualTo(newUsername);
        assertThat(resultSet.getString("surname")).isEqualTo(user.surname());
    }

    @Test
    public void delete_a_person_in_user_table() throws SQLException {
        repository.add(user);
        repository.delete(user.name());

        ResultSet resultSet = executeSelectStatement(user.name());
        assertThat(resultSet.next()).isFalse();
    }

    @After
    public void tearDown() {
        tryConnection()
                .mapTry(Connection::createStatement)
                .andThen(this::dropUsersTable)
                .get();
    }

    private int dropUsersTable(Statement statement) {
        return Try.of(() -> statement.executeUpdate("DROP TABLE Users")).get();
    }

    private ResultSet executeSelectStatement(String username) {
        return tryConnection()
                    .mapTry(Connection::createStatement)
                    .mapTry(statement -> getUsers(statement, username))
                    .get();
    }

    private ResultSet getUsers(Statement statement, String username) {
        return Try.of(() -> statement.executeQuery(selectUsersWithSame(username))).get();
    }

    private String selectUsersWithSame(String username) {
        return format("SELECT * FROM Users WHERE username=\"%s\"", username);
    }

}