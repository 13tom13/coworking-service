package io.ylab.tom13.coworkingservice.out.rest.controller.implementation;

import io.ylab.tom13.coworkingservice.out.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.controller.AuthorizationController;
import io.ylab.tom13.coworkingservice.out.rest.services.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Реализация интерфейса {@link AuthorizationController}.
 * Обрабатывает запросы на аутентификацию пользователя и возвращает соответствующие результаты.
 *
 * @inheritDoc
 */
@RestController
public class AuthorizationControllerSpring implements AuthorizationController {

    private final AuthorizationService authorizationService;

    @Autowired
    public AuthorizationControllerSpring(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PostMapping("/authorization")
    public ResponseEntity<?> login(@RequestBody AuthorizationDTO authorizationDTO) {
        try {
            UserDTO user = authorizationService.login(authorizationDTO);

            String responseSuccess = String.format("Пользователь с именем: %s %s и email: %s успешно авторизирован",
                    user.firstName(), user.lastName(), user.email());

            return ResponseEntity.ok(responseSuccess);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}

