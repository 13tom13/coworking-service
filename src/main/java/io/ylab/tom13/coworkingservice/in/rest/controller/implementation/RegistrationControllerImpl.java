package io.ylab.tom13.coworkingservice.in.rest.controller.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.rest.controller.RegistrationController;
import io.ylab.tom13.coworkingservice.in.rest.services.RegistrationService;
import io.ylab.tom13.coworkingservice.in.rest.services.implementation.RegistrationServiceImpl;

/**
 * Реализация интерфейса {@link RegistrationController}.
 * Обрабатывает запросы на создание нового пользователя и возвращает соответствующие результаты.
 *
 * @inheritDoc
 */
public class RegistrationControllerImpl implements RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationControllerImpl() {
        registrationService = new RegistrationServiceImpl();
    }

    /**
     * @inheritDoc
     */
    @Override
    public ResponseDTO<UserDTO> createUser(final RegistrationDTO registrationDTO) {
        try {
            UserDTO user = registrationService.createUser(registrationDTO);
            return ResponseDTO.success(user);
        } catch (UserAlreadyExistsException | RepositoryException e) {
            return ResponseDTO.failure(e.getMessage());
        }
    }

}

