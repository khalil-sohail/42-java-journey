package fr._42.sockets.server.Connection.enums;

import com.fasterxml.jackson.databind.JsonNode;

public record ProtocolMessage(
    MessageType type,
    JsonNode data
) {}

