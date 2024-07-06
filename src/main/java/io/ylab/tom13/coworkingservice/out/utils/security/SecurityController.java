package io.ylab.tom13.coworkingservice.out.utils.security;

import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.entity.model.User;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.repositories.implementation.UserRepositoryJdbc;
import io.ylab.tom13.coworkingservice.out.utils.Session;

import java.sql.SQLException;
import java.util.Arrays;

import static io.ylab.tom13.coworkingservice.out.database.DatabaseConnection.getConnection;

/**
 * Абстрактный класс для управления безопасностью и доступом.
 */
public abstract class SecurityController {

    private static UserRepository userRepository;
    private static Session session;

    /**
     * Статический блок инициализирует экземпляры UserRepository и Session.
     */
    static {
        try {
            userRepository = new UserRepositoryJdbc(getConnection());
            session = Session.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Проверяет, есть ли у текущего пользователя одна из указанных ролей.
     *
     * @param roles Роли для проверки у текущего пользователя.
     * @return true, если пользователь имеет хотя бы одну из указанных ролей; в противном случае false.
     */
    public static boolean hasRole(Role... roles) {
        return session.getUser()
                .flatMap(userDTO -> userRepository.findById(userDTO.id()))
                .map(User::role)
                .map(userRole -> Arrays.asList(roles).contains(userRole))
                .orElse(false);
    }

    /**
     * Проверяет, аутентифицирован ли текущий пользователь.
     *
     * @return true, если есть аутентифицированная пользовательская сессия; в противном случае false.
     */
    public static boolean hasAuthenticated() {
        return session.getUser().isPresent();
    }

}
