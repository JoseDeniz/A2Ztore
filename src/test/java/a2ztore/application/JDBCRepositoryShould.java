package a2ztore.application;

import a2ztore.model.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static a2ztore.application.DatabaseConnector.stablishConnection;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class JDBCRepositoryShould {

    /**
     * TODO List:
     *
     * - Get a person
     * - Update a person
     * - Remove a person
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

        String sql = format("SELECT * FROM Users WHERE username=\"%s\"", person.name());
        ResultSet resultSet = stablishConnection().createStatement().executeQuery(sql);
        resultSet.next();

        assertThat(resultSet.getString("username")).isEqualTo(person.name());
        assertThat(resultSet.getString("fullname")).isEqualTo(person.fullname());
    }

    @Test
    public void get_a_person_in_user_table() throws SQLException {
        repository.add(person);
        Person databasePerson = repository.get(this.person.name());

        assertThat(databasePerson.name()).isEqualTo(person.name());
        assertThat(databasePerson.fullname()).isEqualTo(person.fullname());
    }

    @After
    public void tearDown() throws SQLException {
        stablishConnection().createStatement().executeUpdate("DROP TABLE Users");
    }

}