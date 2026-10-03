package fr._42.sockets.server.Connection.protocols;

public record StatusResponse(
    boolean success,
    String message
) {}
