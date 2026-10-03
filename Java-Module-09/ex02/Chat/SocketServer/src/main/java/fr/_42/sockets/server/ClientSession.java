package fr._42.sockets.server;

import fr._42.sockets.server.Connection.enums.MessageType;
import fr._42.sockets.server.Connection.JsonConnection;
import fr._42.sockets.models.User;
import fr._42.sockets.models.Room;


import java.io.IOException;

public class ClientSession {
    private final       JsonConnection  jsonConnection;
    private final       User            user;
    private volatile    Room            currentRoom; // Use volatile to ensure visibility across threads

    public ClientSession(JsonConnection jsonConnection, User user) {
        this.jsonConnection = jsonConnection;
        this.user = user;
        this.currentRoom = null;
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
