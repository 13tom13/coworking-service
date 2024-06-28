package io.ylab.tom13.coworkingservice.in.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;


public class AppConfig {
    private static final String CONFIG_FILE = "application.yaml";
    private static final Yaml yaml = new Yaml();

    private static Map<String, Object> config;

    static {
        try (InputStream inputStream = AppConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            config = yaml.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
