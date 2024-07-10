package io.ylab.tom13.coworkingservice.out.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.ylab.tom13.coworkingservice.out.database.LiquibaseConnector;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * Конфигурация приложения для работы с настройками и компонентами.
 */
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

    /**
     * Получить ObjectMapper с настроенным модулем для работы с JavaTime.
     *
     * @return ObjectMapper
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Запуск миграций базы данных с помощью Liquibase.
     */
    public static void liquibaseMigrations() {
        LiquibaseConnector connector = new LiquibaseConnector();
        connector.runMigrations();
    }

    /**
     * Получить конфигурацию базы данных.
     *
     * @return Конфигурация базы данных в виде карты.
     */
    private static Map<String, Object> getDatabaseConfig() {
        return (Map<String, Object>) config.get("database");
    }

    /**
     * Получить URL базы данных.
     *
     * @return URL базы данных.
     */
    public static String getDbUrl() {
        Map<String, Object> databaseConfig = getDatabaseConfig();
        return databaseConfig != null ? (String) databaseConfig.get("url") : null;
    }

    /**
     * Получить имя пользователя базы данных.
     *
     * @return Имя пользователя базы данных.
     */
    public static String getDbUsername() {
        Map<String, Object> databaseConfig = getDatabaseConfig();
        return databaseConfig != null ? (String) databaseConfig.get("username") : null;
    }

    /**
     * Получить пароль базы данных.
     *
     * @return Пароль базы данных.
     */
    public static String getDbPassword() {
        Map<String, Object> databaseConfig = getDatabaseConfig();
        return databaseConfig != null ? (String) databaseConfig.get("password") : null;
    }

    /**
     * Получить конфигурацию Liquibase.
     *
     * @return Конфигурация Liquibase в виде карты.
     */
    private static Map<String, Object> getLiquibaseConfig() {
        return (Map<String, Object>) config.get("liquibase");
    }

    /**
     * Получить схему Liquibase.
     *
     * @return Схема Liquibase.
     */
    public static String getLiquibaseSchema() {
        Map<String, Object> databaseConfig = getLiquibaseConfig();
        return databaseConfig != null ? (String) databaseConfig.get("schema") : null;
    }

    /**
     * Получить changelog Liquibase.
     *
     * @return Changelog Liquibase.
     */
    public static String getLiquibaseChangelog() {
        Map<String, Object> databaseConfig = getLiquibaseConfig();
        return databaseConfig != null ? (String) databaseConfig.get("changelog") : null;
    }

    /**
     * Получить тестовую конфигурацию базы данных.
     *
     * @return Тестовая конфигурация базы данных в виде карты.
     */
    private static Map<String, Object> getTestDatabaseConfig() {
        Map<String, Object> databaseConfig = getDatabaseConfig();
        return databaseConfig != null ? (Map<String, Object>) databaseConfig.get("test") : null;
    }

    /**
     * Получить имя тестовой базы данных.
     *
     * @return Имя тестовой базы данных.
     */
    public static String getTestDatabaseName() {
        Map<String, Object> testConfig = getTestDatabaseConfig();
        return testConfig != null ? (String) testConfig.get("databaseName") : null;
    }

    /**
     * Получить имя пользователя тестовой базы данных.
     *
     * @return Имя пользователя тестовой базы данных.
     */
    public static String getTestDatabaseUsername() {
        Map<String, Object> testConfig = getTestDatabaseConfig();
        return testConfig != null ? (String) testConfig.get("username") : null;
    }

    /**
     * Получить пароль тестовой базы данных.
     *
     * @return Пароль тестовой базы данных.
     */
    public static String getTestDatabasePassword() {
        Map<String, Object> testConfig = getTestDatabaseConfig();
        return testConfig != null ? (String) testConfig.get("password") : null;
    }
}
