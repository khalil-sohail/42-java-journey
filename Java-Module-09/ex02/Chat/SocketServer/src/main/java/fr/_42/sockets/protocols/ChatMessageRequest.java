package fr._42.sockets.server.Connection.protocols;

public record ChatMessageRequest(
    Long roomId,
    String message
) {}
