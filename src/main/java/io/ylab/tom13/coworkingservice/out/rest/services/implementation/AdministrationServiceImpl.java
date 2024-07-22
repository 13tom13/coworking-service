package io.ylab.tom13.coworkingservice.out.rest.services.implementation;

import io.ylab.tom13.coworkingservice.out.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.out.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.out.entity.model.User;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserAlreadyExistsException;
import io.ylab.tom13.coworkingservice.out.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.out.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.out.rest.services.AdministrationService;
import io.ylab.tom13.coworkingservice.out.utils.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Реализация интерфейса {@link AdministrationService}.
 * Сервис администрирования пользователей.
 */
@Service
@RequiredArgsConstructor
public class AdministrationServiceImpl implements AdministrationService {

    private final UserRepository userRepository;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserDTO> getAllUsers() {
        Collection<User> allUsers = userRepository.getAllUsers();
        return allUsers.stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO getUserByEmail(String email) throws UserNotFoundException {
        Optional<User> byEmail = userRepository.findByEmail(email);
        User user = byEmail.orElseThrow(() -> new UserNotFoundException("с email " + email));
        return userMapper.toUserDTO(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO editUserByAdministrator(UserDTO userDTO) throws UserNotFoundException, RepositoryException, UserAlreadyExistsException {
        long id = userDTO.id();
        Optional<User> byId = userRepository.findById(id);
        User userFromRep = byId.orElseThrow(() -> new UserNotFoundException("с ID " + id));
        User userChanged = userMapper.toUserWithPassword(userDTO, userFromRep.password());
        Optional<User> updatedUser = userRepository.updateUser(userChanged);
        if (updatedUser.isPresent()) {
            return userMapper.toUserDTO(userChanged);
        } else {
            throw new RepositoryException("Не удалось обновить пользователя с ID " + userDTO.id());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editUserPasswordByAdministrator(String email, String newHashPassword) throws UserNotFoundException, RepositoryException, UserAlreadyExistsException {
        Optional<User> byEmail = userRepository.findByEmail(email);
        User userFromRep = byEmail.orElseThrow(() -> new UserNotFoundException("с email  " + email));

        User userChanged = userMapper.toUserWithPassword(userFromRep, newHashPassword);
        if (userRepository.updateUser(userChanged).isEmpty()) {
            throw new RepositoryException("Не удалось обновить пароль с email " + email);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public UserDTO registrationUser(RegistrationDTO registrationDTO) throws RepositoryException, UserAlreadyExistsException {
        User newUser = new User(0, registrationDTO.firstName(), registrationDTO.lastName(), registrationDTO.email(),
                registrationDTO.password(), registrationDTO.role());
        Optional<User> registeredUser = userRepository.createUser(newUser);
        if (registeredUser.isEmpty()) {
            throw new RepositoryException("Не удалось создать пользователя");
        }
        return userMapper.toUserDTO(registeredUser.get());
    }
}
