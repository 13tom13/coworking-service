package io.ylab.tom13.coworkingservice.in.rest.controller.user.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.in.rest.controller.user.AuthorizationController;
import io.ylab.tom13.coworkingservice.in.rest.services.user.AuthorizationService;
import io.ylab.tom13.coworkingservice.in.rest.services.user.implementation.AuthorizationServiceImpl;

/**
 * Реализация интерфейса {@link AuthorizationController}.
 * Обрабатывает запросы на аутентификацию пользователя и возвращает соответствующие результаты.
 *
 * @inheritDoc
 */
public class AuthorizationControllerImpl implements AuthorizationController {

    private final AuthorizationService authorizationService;

    public AuthorizationControllerImpl() {
        authorizationService = new AuthorizationServiceImpl();
    }

    /**
     * @inheritDoc
     */
    @Override
    public ResponseDTO<UserDTO> login(AuthorizationDTO authorizationDTO) {
        try {
            UserDTO userDTO = authorizationService.login(authorizationDTO);
            return ResponseDTO.success(userDTO);
        } catch (UnauthorizedException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }
}

