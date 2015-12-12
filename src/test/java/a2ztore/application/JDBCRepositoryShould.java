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
     * - Update a person
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
        ResultSet resultSet = executeSelectStatement();

        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getString("username")).isEqualTo(person.name());
        assertThat(resultSet.getString("fullname")).isEqualTo(person.fullname());
    }

    @Test
    public void get_a_person_in_user_table() throws SQLException {
        repository.add(person);
        Person databasePerson = repository.get(person.name());

        assertThat(databasePerson.name()).isEqualTo(person.name());
        assertThat(databasePerson.fullname()).isEqualTo(person.fullname());
    }

    @Test
    public void delete_a_person_in_user_table() throws SQLException {
        repository.add(person);
        repository.delete(person.name());

        ResultSet resultSet = executeSelectStatement();
        assertThat(resultSet.next()).isFalse();
    }

    @After
    public void tearDown() throws SQLException {
        Statement statement = stablishConnection().createStatement();
        statement.executeUpdate("DROP TABLE Users");
    }

    private ResultSet executeSelectStatement() throws SQLException {
        String sql = format("SELECT * FROM Users WHERE username=\"%s\"", person.name());
        return stablishConnection().createStatement().executeQuery(sql);
    }

}