package fr._42.sockets.server.Connection.enums;

public record ChatMessageRequest(
    Long roomId,
    String message
) {}
