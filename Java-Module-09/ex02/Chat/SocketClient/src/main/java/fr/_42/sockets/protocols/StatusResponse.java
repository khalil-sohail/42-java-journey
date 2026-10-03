package fr._42.sockets.protocols;

public record StatusResponse(
    boolean success,
    String message
) {}
