package fr._42.sockets.services;

import fr._42.sockets.models.Message;
import fr._42.sockets.models.User;
import fr._42.sockets.models.Room;

import java.util.List;

public interface MessagesService {
    void sendMessage(Room room, User sender, String text);
    List<Message> getLastMessages(Long roomId, int limit);
}
