package io.ylab.tom13.coworkingservice.out.database;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Инициализатор Liquibase для запуска миграций при запуске приложения.
 */
@Component
public class LiquibaseInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final LiquibaseConnector liquibaseConnector;

    /**
     * Конструктор инициализатора, принимающий в качестве параметра объект
     * для подключения к Liquibase.
     *
     * @param liquibaseConnector объект для подключения к Liquibase
     */
    public LiquibaseInitializer(LiquibaseConnector liquibaseConnector) {
        this.liquibaseConnector = liquibaseConnector;
    }

    /**
     * Метод, вызываемый при событии обновления контекста приложения.
     * Запускает миграции Liquibase.
     *
     * @param event событие обновления контекста приложения
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        liquibaseConnector.runMigrations();
    }
}
