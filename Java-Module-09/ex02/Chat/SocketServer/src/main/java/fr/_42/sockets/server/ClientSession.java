package fr._42.sockets.server;

import fr._42.sockets.server.Connection.JsonConnection;
import fr._42.sockets.server.Connection.protocols.MessageType;
import fr._42.sockets.models.User;
import fr._42.sockets.models.Room;


import java.io.IOException;

public class ClientSession {
    private final       JsonConnection  jsonConnection;
    private volatile    Room            currentRoom;
    private final       User            user;

    public ClientSession(JsonConnection jsonConnection, User user) {
        this.jsonConnection = jsonConnection;
        this.currentRoom = null;
        this.user = user;
    }

    public JsonConnection   getJsonConnection() { return jsonConnection; }
    public Room             getCurrentRoom()    { return currentRoom; }
    public User             getUser()           { return user; }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public void send(MessageType messageType, Object message) throws IOException {
        jsonConnection.send(messageType, message);
    }
}
