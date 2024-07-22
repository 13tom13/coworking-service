package io.ylab.tom13.coworkingservice.out.rest.services.implementation;

import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.out.entity.model.User;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.services.RegistrationService;
import io.ylab.tom13.coworkingservice.out.utils.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static io.ylab.tom13.coworkingservice.out.security.PasswordUtil.hashPassword;

/**
 * Реализация интерфейса {@link RegistrationService}.
 * Сервиса регистрации пользователей.
 */
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO createUser(RegistrationDTO registrationDTO) {
        String email = registrationDTO.email();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException(email);
        }
        String hashPassword = hashPassword(registrationDTO.password());
        User newUser = new User(0, registrationDTO.firstName(), registrationDTO.lastName(), registrationDTO.email(), hashPassword, Role.USER);
        Optional<User> userFromRep = userRepository.createUser(newUser);
        if (userFromRep.isEmpty()) {
            throw new RepositoryException("Не удалось создать пользователя");
        }
        return userMapper.toUserDTO(userFromRep.get());
    }
}

