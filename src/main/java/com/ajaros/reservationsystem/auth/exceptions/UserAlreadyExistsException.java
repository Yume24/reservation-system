package com.ajaros.reservationsystem.auth.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("User already exists");
    }

    public UserAlreadyExistsException(String email) {
        super("User with email " + email + " already exists");
    }
}
