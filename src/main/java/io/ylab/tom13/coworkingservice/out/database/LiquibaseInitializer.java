package io.ylab.tom13.coworkingservice.out.database;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Инициализатор Liquibase для запуска миграций при запуске приложения.
 */
@Component
@RequiredArgsConstructor
public class LiquibaseInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final LiquibaseConnector liquibaseConnector;

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
