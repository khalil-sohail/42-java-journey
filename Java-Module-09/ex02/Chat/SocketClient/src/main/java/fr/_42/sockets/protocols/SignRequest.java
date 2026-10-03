package fr._42.sockets.protocols;

public record SignRequest(
    String username,
    String password
) {}
