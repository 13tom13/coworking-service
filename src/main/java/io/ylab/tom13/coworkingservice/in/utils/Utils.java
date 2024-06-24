package io.ylab.tom13.coworkingservice.in.utils;

import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;
import io.ylab.tom13.coworkingservice.in.rest.repositories.UserRepository;

public class Utils {


    public static boolean hasRole(long userId, Role role, UserRepository userRepository) {
        return userRepository.findById(userId)
                .map(user -> user.role().equals(role))
                .orElse(false);
    }
}
