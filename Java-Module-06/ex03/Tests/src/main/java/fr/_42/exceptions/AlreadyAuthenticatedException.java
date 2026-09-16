package fr._42.exceptions;

public class AlreadyAuthenticatedException extends RuntimeException {
    public AlreadyAuthenticatedException() {
        super("User is already authenticated.");
    }
}
