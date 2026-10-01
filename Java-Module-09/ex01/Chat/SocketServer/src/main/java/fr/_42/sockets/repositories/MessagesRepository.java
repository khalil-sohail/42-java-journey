package fr._42.sockets.repositories;

import fr._42.sockets.models.Message;

public interface MessagesRepository {
    void save(Message message);
}
