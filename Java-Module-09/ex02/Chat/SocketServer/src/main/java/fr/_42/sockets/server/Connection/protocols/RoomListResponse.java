package fr._42.sockets.server.Connection.protocols;

import java.util.List;

public record RoomListResponse(
    List<RoomInfo> rooms
) {}
