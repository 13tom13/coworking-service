package database;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static io.ylab.tom13.coworkingservice.out.config.ApplicationConfig.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class TestcontainersConnector {

    private final static String TEST_CHANGELOG = getLiquibaseChangelog();
    private final static String TEST_DATABASE = getTestDatabaseName();
    private final static String TEST_USER = getTestDatabaseUsername();
    private final static String TEST_PASSWORD = getTestDatabasePassword();

    private Connection connection;

    protected Connection getTestConnection() {
        return connection;
    }

    @Container
    public PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName(TEST_DATABASE)
            .withUsername(TEST_USER)
            .withPassword(TEST_PASSWORD);


    @BeforeEach
    void setUp() throws SQLException, LiquibaseException {
        postgresContainer.start();
        connection = DriverManager.getConnection(postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword());

        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

        Liquibase liquibase = new Liquibase(TEST_CHANGELOG, new ClassLoaderResourceAccessor(), database);
        liquibase.update();
    }


    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        if (postgresContainer != null) {
            postgresContainer.stop();
        }
    }
}
