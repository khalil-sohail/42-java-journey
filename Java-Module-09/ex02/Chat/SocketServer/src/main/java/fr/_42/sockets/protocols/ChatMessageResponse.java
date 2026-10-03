package fr._42.sockets.server.Connection.protocols;

public record ChatMessageResponse(
    Long roomId,
    String roomName,
    String username,
    String message
) {}
