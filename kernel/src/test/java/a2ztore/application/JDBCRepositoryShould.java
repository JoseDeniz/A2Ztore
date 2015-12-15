package a2ztore.application;

import a2ztore.model.Person;
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

    private Person person;
    private Repository repository;

    @Before
    public void setUp() {
        person = new Person("Phillip", "Fry");
        repository = new JDBCRepository();
    }

    @Test
    public void insert_a_person_in_user_table() throws SQLException {
        repository.add(person);

        ResultSet resultSet = executeSelectStatement(person.name());

        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getString("username")).isEqualTo(person.name());
        assertThat(resultSet.getString("surname")).isEqualTo(person.surname());
    }

    @Test
    public void get_a_person_in_user_table() throws SQLException {
        repository.add(person);
        Person databasePerson = repository.get(person.name());

        assertThat(databasePerson.name()).isEqualTo(person.name());
        assertThat(databasePerson.surname()).isEqualTo(person.surname());
    }

    @Test
    public void update_the_name_of_the_user() throws SQLException {
        repository.add(person);
        String newUsername = "Phillip J";
        repository.update(person.name(), newUsername);
        ResultSet resultSet = executeSelectStatement(newUsername);

        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getString("username")).isEqualTo(newUsername);
        assertThat(resultSet.getString("surname")).isEqualTo(person.surname());
    }

    @Test
    public void delete_a_person_in_user_table() throws SQLException {
        repository.add(person);
        repository.delete(person.name());

        ResultSet resultSet = executeSelectStatement(person.name());
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