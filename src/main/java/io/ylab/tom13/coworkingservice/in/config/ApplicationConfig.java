package io.ylab.tom13.coworkingservice.in.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.ylab.tom13.coworkingservice.in.database.LiquibaseConnector;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;


public class ApplicationConfig {
    private static final String CONFIG_FILE = "application.yaml";
    private static final Yaml yaml = new Yaml();

    private static Map<String, Object> config;

    static {
        try (InputStream inputStream = ApplicationConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            config = yaml.load(inputStream);
        } catch (Exception e) {
            System.err.println("Ошибка чтения конфигурационного файла: " + e.getMessage());
        }
    }

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static void liquibaseMigrations() {
        LiquibaseConnector connector = new LiquibaseConnector();
        connector.runMigrations();
    }

    private static Map<String, Object> getDatabaseConfig() {
        return (Map<String, Object>) config.get("database");
    }

    public static String getDbUrl() {
        Map<String, Object> databaseConfig = getDatabaseConfig();
        return databaseConfig != null ? (String) databaseConfig.get("url") : null;
    }

    public static String getDbUsername() {
        Map<String, Object> databaseConfig = getDatabaseConfig();
        return databaseConfig != null ? (String) databaseConfig.get("username") : null;
    }

    public static String getDbPassword() {
        Map<String, Object> databaseConfig = getDatabaseConfig();
        return databaseConfig != null ? (String) databaseConfig.get("password") : null;
    }


    private static Map<String, Object> getLiquibaseConfig() {
        return (Map<String, Object>) config.get("liquibase");
    }

    public static String getLiquibaseSchema() {
        Map<String, Object> databaseConfig = getLiquibaseConfig();
        return databaseConfig != null ? (String) databaseConfig.get("schema") : null;
    }

    public static String getLiquibaseChangelog() {
        Map<String, Object> databaseConfig = getLiquibaseConfig();
        return databaseConfig != null ? (String) databaseConfig.get("changelog") : null;
    }

    private static Map<String, Object> getTestDatabaseConfig() {
        Map<String, Object> databaseConfig = getDatabaseConfig();
        return databaseConfig != null ? (Map<String, Object>) databaseConfig.get("test") : null;
    }

    public static String getTestDatabaseName() {
        Map<String, Object> testConfig = getTestDatabaseConfig();
        return testConfig != null ? (String) testConfig.get("databaseName") : null;
    }

    public static String getTestDatabaseUsername() {
        Map<String, Object> testConfig = getTestDatabaseConfig();
        return testConfig != null ? (String) testConfig.get("username") : null;
    }

    public static String getTestDatabasePassword() {
        Map<String, Object> testConfig = getTestDatabaseConfig();
        return testConfig != null ? (String) testConfig.get("password") : null;
    }


}
