package fr._42.sockets.protocols;

import java.util.List;

public record RoomListResponse(
    List<RoomInfo> rooms
) {}
