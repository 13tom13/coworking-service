package io.ylab.tom13.coworkingservice.out.database;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class LiquibaseInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final LiquibaseConnector liquibaseConnector;

    public LiquibaseInitializer(LiquibaseConnector liquibaseConnector) {
        this.liquibaseConnector = liquibaseConnector;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        liquibaseConnector.runMigrations();
        ApplicationContext applicationContext = event.getApplicationContext();

    }
}
