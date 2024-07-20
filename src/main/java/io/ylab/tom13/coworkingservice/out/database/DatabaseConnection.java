package io.ylab.tom13.coworkingservice.out.database;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Класс для управления подключением к базе данных PostgreSQL.
 */
@Component
public class DatabaseConnection {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseConnection(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Не удалось загрузить PostgreSQL JDBC драйвер", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return jdbcTemplate.getDataSource().getConnection();
    }
}
