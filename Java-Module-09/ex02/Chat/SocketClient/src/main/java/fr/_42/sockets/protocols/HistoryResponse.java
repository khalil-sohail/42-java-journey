package fr._42.sockets.protocols;

import java.util.List;

public record HistoryResponse(
    Long    roomId,
    String  roomName,
    List<ChatMessageResponse> messages
) {}
