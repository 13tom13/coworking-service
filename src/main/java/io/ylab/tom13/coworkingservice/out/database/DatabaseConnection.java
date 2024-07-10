package io.ylab.tom13.coworkingservice.out.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс для управления подключением к базе данных PostgreSQL.
 */
@Component
public class DatabaseConnection {

    @Value("${database.url}")
    private String dbUrl;

    @Value("${database.username}")
    private String dbUsername;

    @Value("${database.password}")
    private String dbPassword;

    @PostConstruct
    private void loadDriver() {
        try {
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
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }
}
