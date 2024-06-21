package io.ylab.tom13.coworkingservice.in.entity.model;

public record User(
        long id,
        String firstName,
        String lastName,
        String email,
        String password) {
}
