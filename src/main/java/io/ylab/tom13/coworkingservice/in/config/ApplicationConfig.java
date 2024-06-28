package io.ylab.tom13.coworkingservice.in.config;

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


}
