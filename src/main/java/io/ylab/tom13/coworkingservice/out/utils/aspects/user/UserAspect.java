package io.ylab.tom13.coworkingservice.out.utils.aspects.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static io.ylab.tom13.coworkingservice.out.database.DatabaseConnection.getConnection;

/**
 * Абстрактный аспект, предоставляющий функциональность для логирования действий пользователей в базу данных.
 */
public abstract class UserAspect {

    protected static final Logger logger = LoggerFactory.getLogger(UserAspect.class);

    protected Connection connection;

    /**
     * Конструктор, инициализирующий соединение с базой данных.
     */
    protected UserAspect() {
        try {
            this.connection = getConnection();
        } catch (SQLException e) {
            logger.error("Ошибка при соединении к БД: " + e.getMessage());
        }
    }

    /**
     * Логирует действие пользователя в базу данных.
     *
     * @param email  электронная почта пользователя, выполняющего действие
     * @param action описание действия пользователя
     */
    protected void logToDatabase(String email, String action) {
        String sql = """
                INSERT INTO log.logger (date_time, email, action)
                VALUES (?, ?, ?)
                """;
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
                statement.setTimestamp(1, timestamp);
                statement.setString(2, email);
                statement.setString(3, action);

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    connection.rollback();
                    logger.error("Ошибка при попытке сохранить запись");
                }

            } catch (SQLException e) {
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            logger.error("Ошибка при соединении к БД");
        }
    }
}
