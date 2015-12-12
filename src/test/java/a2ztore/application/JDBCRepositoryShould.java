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

    private Person person;

    public static final String DB_URL = "jdbc:mysql://localhost:3306/a2ztoreDB";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "secret";

    /**
     * TODO List:
     *
     * - Get a person
     * - Update a person
     * - Remove a person
     */

    @Before
    public void setUp() {
        person = new Person("Phillip", "Fry");
    }

    @Test
    public void insert_a_person_in_table_users() throws SQLException, InterruptedException {
        JDBCRepository repository = new JDBCRepository();
        repository.add(person);

        Connection connection = getConnection(DB_URL, USERNAME, PASSWORD);
        String sql = "SELECT * FROM Users WHERE username=" + format("\"%s\"", person.name());
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        resultSet.next();
        assertThat(resultSet.getString("username")).isEqualTo(person.name());
        assertThat(resultSet.getString("fullname")).isEqualTo(person.fullname());
    }

    @After
    public void tearDown() throws SQLException {
        Connection connection = getConnection(DB_URL, USERNAME, PASSWORD);
        connection.createStatement().executeUpdate("DROP TABLE Users");
    }
}