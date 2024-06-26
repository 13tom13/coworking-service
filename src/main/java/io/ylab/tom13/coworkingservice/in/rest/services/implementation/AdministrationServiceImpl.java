package io.ylab.tom13.coworkingservice.in.rest.services.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.RegistrationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.UserNotFoundException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.services.AdministrationService;
import io.ylab.tom13.coworkingservice.in.utils.mapper.UserMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class AdministrationServiceImpl implements AdministrationService {

    private final UserRepository userRepository;

    public AdministrationServiceImpl() {
        userRepository = UserRepositoryCollection.getInstance();
    }

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public List<UserDTO> getAllUsers() {

        Collection<User> allUsers = userRepository.getAllUsers();

        return allUsers.stream()
                .map(UserMapper.INSTANCE::toUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserByEmail(String email) throws UserNotFoundException {
        Optional<User> byEmail = userRepository.findByEmail(email);
        User user = byEmail.orElseThrow(() -> new UserNotFoundException("с email " + email));

        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO editUserByAdministrator(UserDTO userDTO) throws UserNotFoundException, RepositoryException {
        long id = userDTO.id();
        Optional<User> byId = userRepository.findById(id);
        User userFromRep = byId.orElseThrow(() -> new UserNotFoundException("с ID " + id));
        User userChanged = new User(userDTO.id(), userDTO.firstName(), userDTO.lastName(), userDTO.email(), userFromRep.password(), userDTO.role());
        Optional<User> updatedUser = userRepository.updateUser(userChanged);
        if (updatedUser.isPresent()) {
            return userMapper.toUserDTO(userChanged);
        } else throw new RepositoryException("Не удалось обновить пользователя с ID " + userDTO.id());
    }

    @Override
    public void editUserPasswordByAdministrator(long userId, String newHashPassword) throws UserNotFoundException, RepositoryException {
        Optional<User> byId = userRepository.findById(userId);
        User userFromRep = byId.orElseThrow(() -> new UserNotFoundException("с ID  " + userId));
        User userChanged = new User(userFromRep.id(), userFromRep.firstName(), userFromRep.lastName(), userFromRep.email(), newHashPassword, userFromRep.role());
        if (userRepository.updateUser(userChanged).isEmpty())
            throw new RepositoryException("Не удалось обновить пароль с ID " + userId);
    }

    @Override
    public void registrationUser(RegistrationDTO registrationDTO, Role role) throws RepositoryException {
        User newUser = new User(0, registrationDTO.firstName(), registrationDTO.lastName(), registrationDTO.email(),
                registrationDTO.password(), role);
        userRepository.createUser(newUser);
    }
}
