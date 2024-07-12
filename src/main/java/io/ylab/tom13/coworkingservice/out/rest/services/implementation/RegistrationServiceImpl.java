package io.ylab.tom13.coworkingservice.out.rest.services.implementation;

import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.entity.model.User;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.services.RegistrationService;
import io.ylab.tom13.coworkingservice.out.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Реализация интерфейса {@link RegistrationService}.
 * Сервиса регистрации пользователей.
 */
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;

    public final UserMapper userMapper = UserMapper.INSTANCE;

    /**
     * Конструктор для инициализации сервиса регистрации с использованием репозитория пользователей.
     */
    @Autowired
    public RegistrationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO createUser(RegistrationDTO registrationDTO) throws UserAlreadyExistsException, RepositoryException {
        String email = registrationDTO.email();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException(email);
        }

        User newUser = new User(0, registrationDTO.firstName(), registrationDTO.lastName(), registrationDTO.email(), registrationDTO.password(), Role.USER);
        Optional<User> userFromRep = userRepository.createUser(newUser);
        if (userFromRep.isEmpty()) {
            throw new RepositoryException("Не удалось создать пользователя");
        }
        return userMapper.toUserDTO(userFromRep.get());
    }
}

