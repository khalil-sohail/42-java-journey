package fr._42.chat.models;

public class NotSavedSubEntityException extends RuntimeException {
    public NotSavedSubEntityException(String message) {
        super(message);
    }
}