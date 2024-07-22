package io.ylab.tom13.coworkingservice.out.utils.audit.repository.implementation;

import io.ylab.tom13.coworkingservice.out.database.DatabaseConnection;
import io.ylab.tom13.coworkingservice.out.utils.audit.repository.UserAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;


/**
 * Реализация интерфейса {@link UserAuditRepository}, предоставляющая методы для записи действий пользователя в базу данных.
 *
 * <p>Этот класс использует {@link DatabaseConnection} для получения соединения с базой данных и выполняет SQL-запросы
 * для создания таблиц, последовательностей и вставки данных о действиях пользователя.</p>
 *
 * <p>Аннотации:</p>
 * <ul>
 *     <li>{@link Repository} - обозначает, что этот класс является репозиторием Spring.</li>
 *     <li>{@link Slf4j} - генерирует логгер SLF4J для этого класса.</li>
 *     <li>{@link RequiredArgsConstructor} - создает конструктор с требуемыми зависимостями.</li>
 * </ul>
 *
 * @see UserAuditRepository
 * @see DatabaseConnection
 */
@Slf4j
@RequiredArgsConstructor
@Repository
public class UserAuditRepositoryImpl implements UserAuditRepository {

    private static final String SCHEMA_NAME = "log";
    private static final String TABLE = "TABLE";
    private static final String SEQUENCE = "SEQUENCE";
    private static final String TABLE_NAME= "id_";

    private final DatabaseConnection databaseConnection;

    /**
     * {@inheritDoc}
     * <p>Этот метод создает таблицу и последовательность для логирования действий пользователя, если они не существуют,
     * а затем вставляет запись действия пользователя в таблицу.</p>
     */
    @Override
    public void logToDatabase(long id, String action) {
        String tableName = SCHEMA_NAME + "." + TABLE_NAME + id;
        String sequenceName = SCHEMA_NAME + ".log_"+ TABLE_NAME + id + "_seq";

        createSequenceIfNotExists(sequenceName);
        createTableIfNotExists(tableName, sequenceName);

        String sql = String.format("""
                INSERT INTO %s (date, user_id, action)
                VALUES (?, ?, ?)
                """, tableName);

        try (Connection connection = databaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
                statement.setTimestamp(1, timestamp);
                statement.setLong(2, id);
                statement.setString(3, action);

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    connection.rollback();
                    log.error("Ошибка при попытке сохранить запись");
                } else {
                    connection.commit();
                }
            } catch (SQLException e) {
                connection.rollback();
                log.error("Ошибка при выполнении SQL-запроса: {}", e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            log.error("Ошибка при соединении к БД: {}", e.getMessage());
        }
    }

    /**
     * Создает таблицу для логирования действий пользователя, если она не существует.
     *
     * @param tableName    имя таблицы
     * @param sequenceName имя последовательности
     */
    private void createTableIfNotExists(String tableName, String sequenceName) {
        try (Connection connection = databaseConnection.getConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            try (ResultSet resultSet = meta.getTables(null, SCHEMA_NAME, tableName, new String[]{TABLE})) {
                if (!resultSet.next()) {
                    String createSchemaSql = "CREATE SCHEMA IF NOT EXISTS log";
                    try (Statement statement = connection.createStatement()) {
                        statement.execute(createSchemaSql);
                    } catch (SQLException ignored) {
                    }

                    String createTableSql = String.format("""
                            CREATE TABLE %s (
                            id BIGINT PRIMARY KEY DEFAULT NEXTVAL('%s'),
                            date TIMESTAMP,
                            user_id BIGINT,
                            action VARCHAR(255)
                            )
                            """, tableName, sequenceName);
                    try (Statement statement = connection.createStatement()) {
                        statement.executeUpdate(createTableSql);
                    } catch (SQLException ignored) {
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при проверке существования таблицы {}: {}", tableName, e.getMessage());
        }
    }

    /**
     * Создает последовательность для генерации идентификаторов записей логирования, если она не существует.
     *
     * @param sequenceName имя последовательности
     */
    private void createSequenceIfNotExists(String sequenceName) {
        try (Connection connection = databaseConnection.getConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            try (ResultSet resultSet = meta.getTables(null, SCHEMA_NAME, sequenceName, new String[]{SEQUENCE})) {
                if (!resultSet.next()) {
                    String createSequenceSql = String.format("""
                            CREATE SEQUENCE %s
                            START WITH 1
                            INCREMENT BY 1
                            """, sequenceName);
                    try (Statement statement = connection.createStatement()) {
                        statement.executeUpdate(createSequenceSql);
                    } catch (SQLException ignored) {
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при проверке существования последовательности {}: {}", sequenceName, e.getMessage());
        }
    }
}

