package io.ylab.tom13.coworkingservice;

import io.ylab.tom13.coworkingservice.out.CoworkingServiceApplication;

import java.sql.SQLException;

import static io.ylab.tom13.coworkingservice.in.database.DatabaseConnection.getConnection;

/**
 * Главный класс приложения, запускающий CoworkingServiceApplication.
 */
public class Main {

    /**
     * Основной метод приложения, запускающий CoworkingServiceApplication.
     */
    public static void main(String[] args) {
        try {
            System.out.println(getConnection().getCatalog());
            new CoworkingServiceApplication().start();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

