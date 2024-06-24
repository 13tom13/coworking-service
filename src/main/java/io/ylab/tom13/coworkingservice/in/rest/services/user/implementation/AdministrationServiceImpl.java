package io.ylab.tom13.coworkingservice.in.rest.services.user.implementation;

import io.ylab.tom13.coworkingservice.in.entity.dto.AuthenticationDTO;
import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.exceptions.security.NoAccessException;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryCollection;
import io.ylab.tom13.coworkingservice.in.rest.services.user.AdministrationService;
import io.ylab.tom13.coworkingservice.in.utils.mapper.UserMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static io.ylab.tom13.coworkingservice.in.utils.Utils.hasRole;

public class AdministrationServiceImpl implements AdministrationService {

    private final UserRepository userRepository;

    public AdministrationServiceImpl() {
        userRepository = UserRepositoryCollection.getInstance();
    }

    @Override
    public List<UserDTO> getAllUsers(AuthenticationDTO authenticationDTO) throws NoAccessException {
        long adminId = authenticationDTO.userId();
        if (hasRole(adminId, Role.ADMINISTRATOR, userRepository)) {
            Collection<User> allUsers = userRepository.getAllUsers();
            return allUsers.stream()
                    .map(UserMapper.INSTANCE::toUserDTO)
                    .collect(Collectors.toList());
        } else {
            throw new NoAccessException();
        }
    }
}
