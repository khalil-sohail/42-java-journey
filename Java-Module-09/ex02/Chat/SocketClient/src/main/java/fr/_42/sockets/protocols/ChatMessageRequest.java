package fr._42.sockets.protocols;

public record ChatMessageRequest(
    Long roomId,
    String message
) {}
