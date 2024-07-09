package io.ylab.tom13.coworkingservice.out.database;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static io.ylab.tom13.coworkingservice.out.config.ApplicationConfig.getLiquibaseChangelog;
import static io.ylab.tom13.coworkingservice.out.config.ApplicationConfig.getLiquibaseSchema;
import static io.ylab.tom13.coworkingservice.out.database.DatabaseConnection.getConnection;

/**
 * Класс для выполнения миграций базы данных с помощью Liquibase.
 */
public class LiquibaseConnector {

    private final String changelogFilePath = getLiquibaseChangelog();
    private final String schema = getLiquibaseSchema();

    /**
     * Конструктор по умолчанию.
     */
    public LiquibaseConnector() {
    }

    /**
     * Запускает миграции базы данных с использованием конфигурации Liquibase.
     */
    public void runMigrations() {
        try (Connection connection = getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            createDefaultSchema(connection, schema);
            database.setLiquibaseSchemaName(schema);

            Liquibase liquibase = new Liquibase(changelogFilePath, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Миграция Liquibase выполнена успешно.\n");
        } catch (SQLException | LiquibaseException e) {
            System.err.println("Ошибка миграции Liquibase: " + e.getMessage());
        }
    }

    /**
     * Создает схему в базе данных, если она не существует.
     *
     * @param connection текущее соединение с базой данных
     * @param schemaName имя схемы, которую необходимо создать
     * @throws SQLException если возникает ошибка при выполнении SQL-запроса
     */
    private void createDefaultSchema(Connection connection, String schemaName) throws SQLException {
        String sql = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        }
    }
}
