package fr._42.sockets.server.Connection.protocols;

public record SignRequest(
    String username,
    String password
) {}
