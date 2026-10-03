package fr._42.sockets.server.Connection.enums;

public record ChatMessageResponse(
    Long roomId,
    String roomName,
    String username,
    String message
) {}
