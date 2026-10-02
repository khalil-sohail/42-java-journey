package fr._42.sockets.server;

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

    public void broadcast(String message) {
        sessions.forEach(session -> session.send(message));
    }
}
