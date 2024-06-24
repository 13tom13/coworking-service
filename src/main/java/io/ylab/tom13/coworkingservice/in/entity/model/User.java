package io.ylab.tom13.coworkingservice.in.entity.model;

import io.ylab.tom13.coworkingservice.in.entity.enumeration.Role;

public record User(
        long id,
        String firstName,
        String lastName,
        String email,
        String password,
        Role role) {
}
