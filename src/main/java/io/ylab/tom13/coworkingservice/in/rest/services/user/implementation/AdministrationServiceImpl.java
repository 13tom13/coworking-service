package io.ylab.tom13.coworkingservice.in.rest.services.user.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthenticationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.security.NoAccessException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.services.user.AdministrationService;
import io.ylab.tom13.coworkingservice.in.utils.mapper.UserMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.ylab.tom13.coworkingservice.in.utils.Security.hasRole;

public class AdministrationServiceImpl implements AdministrationService {

    private final UserRepository userRepository;

    public AdministrationServiceImpl() {
        userRepository = UserRepositoryCollection.getInstance();
    }

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public List<UserDTO> getAllUsers(AuthenticationDTO authentication) throws NoAccessException {
        if (hasRole(authentication.userId(),userRepository,Role.ADMINISTRATOR)){
            Collection<User> allUsers = userRepository.getAllUsers();

            return allUsers.stream()
                    .map(UserMapper.INSTANCE::toUserDTO)
                    .collect(Collectors.toList());

        } else throw new NoAccessException();
    }

    @Override
    public UserDTO getUserByEmail(AuthenticationDTO authentication, String email) throws UserNotFoundException, NoAccessException {
        if (hasRole(authentication.userId(),userRepository, Role.ADMINISTRATOR)){
            Optional<User> byEmail = userRepository.findByEmail(email);
            User user = byEmail.orElseThrow(() -> new UserNotFoundException("с email " + email));

            return userMapper.toUserDTO(user);
        } else throw new NoAccessException();
    }

    @Override
    public UserDTO editUserByAdministrator(AuthenticationDTO authentication, UserDTO userDTO) throws UserNotFoundException, NoAccessException, RepositoryException {
        if (hasRole(authentication.userId(), userRepository, Role.ADMINISTRATOR)){
            long id = userDTO.id();
            Optional<User> byId = userRepository.findById(id);
            User userFromRep = byId.orElseThrow(() -> new UserNotFoundException("с ID " + id));
            User userChanged = new User(userDTO.id(), userDTO.firstName(), userDTO.lastName(), userDTO.email(), userFromRep.password(), userDTO.role());
            Optional<User> updatedUser = userRepository.updateUser(userChanged);
            if (updatedUser.isPresent()){
                return userMapper.toUserDTO(userChanged);
            } else throw new RepositoryException("Не удалось обновить пользователя с ID " + userDTO.id());
        } else throw new NoAccessException();

    }

    @Override
    public void editUserPasswordByAdministrator(AuthenticationDTO authentication, long userId, String newHashPassword) throws NoAccessException, UserNotFoundException, RepositoryException {
        if (hasRole(authentication.userId(), userRepository,  Role.ADMINISTRATOR)){
            Optional<User> byId = userRepository.findById(userId);
            User userFromRep  = byId.orElseThrow(()  -> new UserNotFoundException("с ID  " + userId));
            User userChanged = new User(userFromRep.id(), userFromRep.firstName(), userFromRep.lastName(), userFromRep.email(), newHashPassword, userFromRep.role());
            if (userRepository.updateUser(userChanged).isEmpty())
                throw new RepositoryException("Не удалось обновить пароль с ID " + userId);
        } else throw new NoAccessException();
    }

    @Override
    public void registrationUser(AuthenticationDTO authentication, RegistrationDTO registrationDTO, Role role) throws NoAccessException, RepositoryException {
        if (hasRole(authentication.userId(), userRepository,  Role.ADMINISTRATOR)){
            User newUser = new User(0, registrationDTO.firstName(), registrationDTO.lastName(), registrationDTO.email(),
                    registrationDTO.password(), role);
            userRepository.createUser(newUser);
        } else throw new NoAccessException();
    }
}
