package fr._42.sockets.services;

import fr._42.sockets.models.User;

public interface MessagesService {
    void sendMessage(User sender, String text);
}
