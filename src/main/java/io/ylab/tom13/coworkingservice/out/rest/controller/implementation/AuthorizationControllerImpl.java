package io.ylab.tom13.coworkingservice.out.rest.controller.implementation;

import io.ylab.tom13.coworkingservice.out.entity.dto.AuthorizationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.security.UnauthorizedException;
import io.ylab.tom13.coworkingservice.out.rest.controller.AuthorizationController;
import io.ylab.tom13.coworkingservice.out.rest.services.AuthorizationService;
import io.ylab.tom13.coworkingservice.out.rest.services.implementation.AuthorizationServiceImpl;

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
     * {@inheritDoc}
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

