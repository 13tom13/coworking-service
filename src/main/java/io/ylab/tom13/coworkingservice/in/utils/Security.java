package io.ylab.tom13.coworkingservice.in.utils;

import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.entity.model.User;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;

import java.util.Optional;

public class Security {

    private Security() {
    }

    public static boolean hasRole(long userId, UserRepository userRepository, Role... roles) {
        Optional<User> userOptional = userRepository.findById(userId);

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
}
