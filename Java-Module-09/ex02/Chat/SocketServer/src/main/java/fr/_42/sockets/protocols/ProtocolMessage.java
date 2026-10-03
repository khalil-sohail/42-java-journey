package fr._42.sockets.server.Connection.protocols;

import com.fasterxml.jackson.databind.JsonNode;

public record ProtocolMessage(
    MessageType type,
    JsonNode data
) {}

