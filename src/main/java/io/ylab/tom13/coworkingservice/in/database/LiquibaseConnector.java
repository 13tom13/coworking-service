package io.ylab.tom13.coworkingservice.in.database;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static io.ylab.tom13.coworkingservice.in.config.ApplicationConfig.getLiquibaseChangelog;
import static io.ylab.tom13.coworkingservice.in.config.ApplicationConfig.getLiquibaseSchema;
import static io.ylab.tom13.coworkingservice.in.database.DatabaseConnection.getConnection;

public class LiquibaseConnector {

    private final String changelogFilePath = getLiquibaseChangelog();
    private final String schema = getLiquibaseSchema();


    public LiquibaseConnector() {
    }

    public void runMigrations() {
        try (Connection connection = getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            createDefaultSchema(connection, schema);
            database.setLiquibaseSchemaName(schema);

            Liquibase liquibase = new Liquibase(changelogFilePath, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Миграция Liquibase выполнена успешно.\n");

        } catch (SQLException | LiquibaseException e) {

            System.out.println("Ошибка миграции Liquibase:" + e.getMessage());
        }
    }

    private void createDefaultSchema(Connection connection, String schemaName) throws SQLException {
        String sql = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.execute();
    }

}
