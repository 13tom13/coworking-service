package io.ylab.tom13.coworkingservice.in.security;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryJdbc;
import io.ylab.tom13.coworkingservice.out.utils.Session;

import java.sql.SQLException;
import java.util.Optional;

import static io.ylab.tom13.coworkingservice.in.database.DatabaseConnection.getConnection;

/**
 * Абстрактный класс для управления безопасностью и доступом.
 */
public abstract class SecurityController {

    private UserRepository userRepository;
    private Session session;

    /**
     * Конструктор инициализирует экземпляры UserRepository и Session.
     */
    protected SecurityController() {
        try {
            userRepository = new UserRepositoryJdbc(getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        session = Session.getInstance();
    }

    /**
     * Проверяет, есть ли у текущего пользователя одна из указанных ролей.
     *
     * @param roles Роли для проверки у текущего пользователя.
     * @return true, если пользователь имеет хотя бы одну из указанных ролей; в противном случае false.
     */
    public boolean hasRole(Role... roles) {
        UserDTO user = session.getUser();
        Optional<User> userOptional = userRepository.findById(user.id());

        if (userOptional.isEmpty()) {
            return false;
        }

        Role userRole = userOptional.get().role();

        for (Role role : roles) {
            if (userRole.equals(role)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Проверяет, аутентифицирован ли текущий пользователь.
     *
     * @return true, если есть аутентифицированная пользовательская сессия; в противном случае false.
     */
    public boolean hasAuthenticated() {
        return session.getUser() != null;
    }

}
