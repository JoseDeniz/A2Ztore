package a2ztore.application;

import a2ztore.model.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static a2ztore.application.DatabaseConnector.stablishConnection;
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
    private JDBCRepository repository;

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
    public void tearDown() throws SQLException {
        Statement statement = stablishConnection().createStatement();
        statement.executeUpdate("DROP TABLE Users");
    }

    private ResultSet executeSelectStatement(String username) throws SQLException {
        String sql = format("SELECT * FROM Users WHERE username=\"%s\"", username);
        return stablishConnection().createStatement().executeQuery(sql);
    }

}