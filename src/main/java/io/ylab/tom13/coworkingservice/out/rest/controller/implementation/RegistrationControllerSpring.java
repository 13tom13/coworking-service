package io.ylab.tom13.coworkingservice.out.rest.controller.implementation;

import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.rest.controller.RegistrationController;
import io.ylab.tom13.coworkingservice.out.rest.services.RegistrationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Реализация интерфейса {@link RegistrationController}.
 * Обрабатывает запросы на создание нового пользователя и возвращает соответствующие результаты.
 */
@RestController
@RequestMapping("/registration")
public class RegistrationControllerSpring implements RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationControllerSpring(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody final RegistrationDTO registrationDTO) {
        try {
            UserDTO user = registrationService.createUser(registrationDTO);
            return ResponseEntity.status(HttpServletResponse.SC_CREATED).body(user);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RepositoryException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}

