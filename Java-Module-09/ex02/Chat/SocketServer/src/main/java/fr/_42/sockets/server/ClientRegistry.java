package fr._42.sockets.server;

import fr._42.sockets.server.Connection.protocols.MessageType;
import fr._42.sockets.models.Room;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

@Component
public class ClientRegistry {
    private final Set<ClientSession> sessions = ConcurrentHashMap.newKeySet();

    public void add(ClientSession session) {
        sessions.add(session);
    }

    public void remove(ClientSession session) {
        sessions.remove(session);
    }

    public void broadcastToRoom(Room room, MessageType messageType, Object message) {
        if (room == null) {
            return;
        }

        sessions.stream()
            .filter(session -> session.getCurrentRoom() != null)
            .filter(session ->
                session.getCurrentRoom()
                    .getIdentifier()
                    .equals(room.getIdentifier())
            )
            .forEach(session -> {
                try {
                    session.send(messageType, message);
                } catch (Exception e) {
                    System.err.println("Failed to send message to user: " + session.getUser().getUsername());
                    e.printStackTrace();
                }
            });
    }
}
