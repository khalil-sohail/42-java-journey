package fr._42.exceptions;

public class IllegalNumberException extends RuntimeException {

    public IllegalNumberException() {
        super("Number must be greater than 1");
    }
}