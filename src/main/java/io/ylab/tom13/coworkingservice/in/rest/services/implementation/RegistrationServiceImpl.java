package io.ylab.tom13.coworkingservice.in.rest.services.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.services.RegistrationService;

/**
 * Реализация сервиса регистрации пользователей.
 */
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;

    /**
     * Конструктор для инициализации сервиса регистрации с использованием репозитория пользователей.
     */
    public RegistrationServiceImpl() {
        userRepository = UserRepositoryCollection.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO createUser(RegistrationDTO registrationDTO) throws UserAlreadyExistsException {
        return userRepository.createUser(registrationDTO);
    }
}

