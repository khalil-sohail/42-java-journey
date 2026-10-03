package fr._42.sockets.server.Connection.enums;

import java.util.List;

public record RoomListResponse(
    List<RoomInfo> rooms
) {}
