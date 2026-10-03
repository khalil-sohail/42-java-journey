package fr._42.sockets.server.Connection.enums;

import java.util.List;

public record HistoryResponse(
    Long    roomId,
    String  roomName,
    List<ChatMessageResponse> messages
) {}
