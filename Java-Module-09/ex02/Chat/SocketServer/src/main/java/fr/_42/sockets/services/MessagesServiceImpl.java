package fr._42.sockets.services;

import fr._42.sockets.repositories.MessagesRepository;
import fr._42.sockets.models.Message;
import fr._42.sockets.models.Room;
import fr._42.sockets.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MessagesServiceImpl implements MessagesService {
    private final MessagesRepository messagesRepository;

    @Autowired
    public MessagesServiceImpl(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    @Override
    public void sendMessage(Room room, User sender, String text) {
        Message message = new Message(
            null,
            room,
            sender,
            text,
            LocalDateTime.now()
        );

        messagesRepository.save(message);
    }

    @Override
    public List<Message> getLastMessages(Long roomId, int limit) {
        return messagesRepository.findLastMessages(roomId, limit);
    }
}