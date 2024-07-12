package io.ylab.tom13.coworkingservice.out.security;

import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.entity.model.User;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import jakarta.servlet.http.HttpServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;


/**
 * Абстрактный класс для управления безопасностью и доступом.
 */
@Component
public abstract class SecurityHTTPController extends HttpServlet {

    private final UserRepository userRepository;

    /**
     * Конструктор инициализирует экземпляры UserRepository и Session.
     */
    @Autowired
    protected SecurityHTTPController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Проверяет, есть ли у текущего пользователя одна из указанных ролей.
     *
     * @param roles Роли для проверки у текущего пользователя.
     * @return true, если пользователь имеет хотя бы одну из указанных ролей; в противном случае false.
     */
    public boolean hasRole(Role... roles) {
        return false;
    }

    /**
     * Проверяет, аутентифицирован ли текущий пользователь.
     *
     * @return true, если есть аутентифицированная пользовательская сессия; в противном случае false.
     */
    public boolean hasAuthenticated() {
        return false;
    }

}
