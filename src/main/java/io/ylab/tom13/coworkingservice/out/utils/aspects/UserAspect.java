package io.ylab.tom13.coworkingservice.out.utils.aspects;

import io.ylab.tom13.coworkingservice.out.database.DatabaseConnection;
import io.ylab.tom13.coworkingservice.out.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Абстрактный аспект, предоставляющий функциональность для логирования действий пользователей в базу данных.
 * Реализации этого аспекта могут использоваться для записи действий пользователей в специальные лог-таблицы,
 * сгенерированные на основе идентификатора пользователя.
 */
@Component
public abstract class UserAspect {

    protected static final Logger logger = LoggerFactory.getLogger(UserAspect.class);

    private final DatabaseConnection databaseConnection;
    protected final JwtUtil jwtUtil;

    /**
     * Конструктор, инъектирующий зависимости для работы с базой данных и JWT утилитой.
     *
     * @param databaseConnection соединение с базой данных
     * @param jwtUtil             утилита для работы с JWT
     */
    @Autowired
    public UserAspect(DatabaseConnection databaseConnection, JwtUtil jwtUtil) {
        this.databaseConnection = databaseConnection;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Логирует действие пользователя в базу данных и в журнал логирования.
     *
     * @param joinPoint        точка присоединения для получения информации о вызываемом методе
     * @param actionDescription описание действия пользователя
     */
    protected void logAction(JoinPoint joinPoint, String actionDescription) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Long userId = jwtUtil.getIdFromToken(token);
            String methodName = joinPoint.getSignature().getName();

            logToDatabase(userId, actionDescription);
            logger.info("Пользователь с ID {} {}.", userId, actionDescription);
        } else {
            logger.warn("Заголовок Authorization не найден или имеет неверный формат.");
        }
    }

    /**
     * Логирует действие пользователя в базу данных.
     *
     * @param id     идентификатор пользователя, выполняющего действие
     * @param action описание действия пользователя
     */
    protected void logToDatabase(long id, String action) {
        String tableName = "log.id_" + id;
        String sequenceName = "log.log_id_" + id + "_seq";

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
                    logger.error("Ошибка при попытке сохранить запись");
                } else {
                    connection.commit();
                }
            } catch (SQLException e) {
                connection.rollback();
                logger.error("Ошибка при выполнении SQL-запроса: {}", e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при соединении к БД: {}", e.getMessage());
        }
    }

    /**
     * Создает таблицу в базе данных, если она не существует.
     *
     * @param tableName    имя таблицы
     * @param sequenceName имя последовательности
     */
    private void createTableIfNotExists(String tableName, String sequenceName) {
        try (Connection connection = databaseConnection.getConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            try (ResultSet resultSet = meta.getTables(null, "log", tableName, new String[]{"TABLE"})) {
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
            logger.error("Ошибка при проверке существования таблицы {}: {}", tableName, e.getMessage());
        }
    }

    /**
     * Создает последовательность в базе данных, если она не существует.
     *
     * @param sequenceName имя последовательности
     */
    private void createSequenceIfNotExists(String sequenceName) {
        try (Connection connection = databaseConnection.getConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            try (ResultSet resultSet = meta.getTables(null, "log", sequenceName, new String[]{"SEQUENCE"})) {
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
            logger.error("Ошибка при проверке существования последовательности {}: {}", sequenceName, e.getMessage());
        }
    }
}