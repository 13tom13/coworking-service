package io.ylab.tom13.coworkingservice.out.rest.controller.implementation;

import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.ResponseDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.rest.controller.RegistrationController;
import io.ylab.tom13.coworkingservice.out.rest.services.RegistrationService;
import io.ylab.tom13.coworkingservice.out.rest.services.implementation.RegistrationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Реализация интерфейса {@link RegistrationController}.
 * Обрабатывает запросы на создание нового пользователя и возвращает соответствующие результаты.
 */
@Controller
public class RegistrationControllerImpl implements RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationControllerImpl(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * {@inheritDoc}
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

