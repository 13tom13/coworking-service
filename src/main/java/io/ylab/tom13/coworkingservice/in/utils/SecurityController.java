package io.ylab.tom13.coworkingservice.in.utils;

import io.ylab.tom13.coworkingservice.in.entity.dto.UserDTO;
import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;
import io.ylab.tom13.coworkingservice.in.rest.repositories.implementation.UserRepositoryCollection;
import io.ylab.tom13.coworkingservice.out.utils.Session;

import java.util.Optional;

public abstract class SecurityController {

    private UserRepository userRepository;

    private Session session;

    protected SecurityController() {
        userRepository = UserRepositoryCollection.getInstance();
        session = Session.getInstance();
    }

    public boolean hasRole(Role... roles) {
        UserDTO user = session.getUser();
        Optional<User> userOptional = userRepository.findById(user.id());

        if (userOptional.isEmpty()) {
            return false;
        }

        Role userRole = userOptional.get().role();

        for (Role role : roles) {
            if (userRole.equals(role)) {
                return true;
            }
        }

        return false;
    }


    public boolean hasAuthenticated() {
        return session.getUser() != null;
    }

}
