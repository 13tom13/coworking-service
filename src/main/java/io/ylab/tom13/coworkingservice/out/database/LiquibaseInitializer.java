package io.ylab.tom13.coworkingservice.out.database;


import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class LiquibaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        liquibaseMigrations();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("goodbye");
    }

    public void liquibaseMigrations() {
        LiquibaseConnector connector = new LiquibaseConnector();
        connector.runMigrations();
    }
}

