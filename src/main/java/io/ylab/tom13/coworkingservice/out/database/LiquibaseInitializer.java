package io.ylab.tom13.coworkingservice.out.database;


import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Класс для инициализации миграций Liquibase при запуске веб-приложения.
 */
@WebListener
public class LiquibaseInitializer implements ServletContextListener {

    /**
     * Метод вызывается при инициализации контекста сервлета.
     *
     * @param sce событие, содержащее информацию о контексте сервлета
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        liquibaseMigrations();
    }

    /**
     * Метод вызывается при уничтожении контекста сервлета.
     *
     * @param sce событие, содержащее информацию о контексте сервлета
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("goodbye");
    }

    /**
     * Запуск миграций базы данных с использованием Liquibase.
     */
    public void liquibaseMigrations() {
        LiquibaseConnector connector = new LiquibaseConnector();
        connector.runMigrations();
    }
}

