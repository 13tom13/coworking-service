package io.ylab.tom13.coworkingservice.out.database;

import io.ylab.tom13.coworkingservice.out.config.ApplicationConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс для управления подключением к базе данных PostgreSQL.
 */
public class DatabaseConnection {

    static {
        try {
            // Загрузка драйвера PostgreSQL JDBC при загрузке класса
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Не удалось загрузить PostgreSQL JDBC драйвер", e);
        }
    }

    /**
     * Получить соединение с базой данных.
     *
     * @return объект Connection для подключения к базе данных
     * @throws SQLException если не удается подключиться к базе данных
     */
    public static Connection getConnection() throws SQLException {
        String url = ApplicationConfig.getDbUrl();
        String username = ApplicationConfig.getDbUsername();
        String password = ApplicationConfig.getDbPassword();
        return DriverManager.getConnection(url, username, password);
    }
}
