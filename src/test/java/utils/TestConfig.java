package utils;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"io.ylab.tom13.coworkingservice.out.rest", "io.ylab.tom13.coworkingservice.out.security"})
public class TestConfig {

//    @Value("${test.databaseName}")
//    private String TEST_DATABASE;
//    @Value("${test.username}")
//    private String TEST_USER;
//    @Value("${test.password}")
//    private String TEST_PASSWORD;
//    @Value("${test.container}")
//    private String POSTGRES_CONTAINER;
//
//    @Bean
//    public DatabaseConnection databaseConnection(PostgreSQLContainer<?> postgresContainer) {
//        if (!postgresContainer.isRunning()) {
//            postgresContainer.start();
//        }
//        return new DatabaseConnection(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword());
//    }
//
//    @Bean
//    public PostgreSQLContainer<?> containerInit() {
//        return new PostgreSQLContainer<>(POSTGRES_CONTAINER)
//                .withDatabaseName(TEST_DATABASE)
//                .withUsername(TEST_USER)
//                .withPassword(TEST_PASSWORD);
//    }
}