package fr._42.sockets.repositories;

import java.util.List;

import fr._42.sockets.models.Message;

public interface MessagesRepository {
    void save(Message message);
    List<Message> findLastMessages(Long roomId, int count);
}
