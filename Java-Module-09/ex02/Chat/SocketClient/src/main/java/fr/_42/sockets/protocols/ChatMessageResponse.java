package fr._42.sockets.protocols;

public record ChatMessageResponse(
    Long roomId,
    String roomName,
    String username,
    String message
) {}
