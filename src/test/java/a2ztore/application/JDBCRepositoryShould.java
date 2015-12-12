package a2ztore.application;

import a2ztore.model.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.String.format;
import static java.sql.DriverManager.getConnection;
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
    public void insert_a_person_in_table_users() throws SQLException, InterruptedException {
        repository.add(person);

        String sql = format("SELECT * FROM Users WHERE username=\"%s\"", person.name());
        ResultSet resultSet = stablishConnection().createStatement().executeQuery(sql);
        resultSet.next();
        
        assertThat(resultSet.getString("username")).isEqualTo(person.name());
        assertThat(resultSet.getString("fullname")).isEqualTo(person.fullname());
    }

    @After
    public void tearDown() throws SQLException {
        stablishConnection().createStatement().executeUpdate("DROP TABLE Users");
    }

    private Connection stablishConnection() throws SQLException {
        return getConnection("jdbc:mysql://localhost:3306/a2ztoreDB", "root", "secret");
    }

}