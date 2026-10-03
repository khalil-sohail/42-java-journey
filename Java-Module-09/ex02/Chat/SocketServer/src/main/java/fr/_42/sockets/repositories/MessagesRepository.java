package fr._42.sockets.repositories;

import fr._42.sockets.models.Message;

import java.util.List;

public interface MessagesRepository {
    void save(Message message);
    List<Message> findLastMessages(Long roomId, int count);
}
