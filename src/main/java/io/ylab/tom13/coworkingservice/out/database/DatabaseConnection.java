package io.ylab.tom13.coworkingservice.out.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс для управления подключением к базе данных PostgreSQL.
 */
@Component
public class DatabaseConnection {

    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    public DatabaseConnection(
            @Value("${database.url}") String dbUrl,
            @Value("${database.username}") String dbUsername,
            @Value("${database.password}") String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }

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
