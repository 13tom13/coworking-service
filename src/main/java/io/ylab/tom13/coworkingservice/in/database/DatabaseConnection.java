package io.ylab.tom13.coworkingservice.in.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static io.ylab.tom13.coworkingservice.in.config.ApplicationConfig.*;

public class DatabaseConnection {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Не удалось загрузить PostgreSQL JDBC драйвер", e);
        }
    }


    public static Connection getConnection() throws SQLException {
        String url = getDbUrl();
        String username = getDbUsername();
        String password = getDbPassword();
        return DriverManager.getConnection(url, username, password);
    }

}
