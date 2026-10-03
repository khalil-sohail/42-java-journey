package fr._42.sockets.services;

import fr._42.sockets.models.Room;
import fr._42.sockets.models.User;

import java.util.List;
import java.util.Optional;

public interface RoomsService {
    Room            createRoom(User user, String name);
    List<Room>      getRooms();
    Optional<Room>  chooseRoom(User user, Long roomId);
    Optional<Room>  getLastRoom(User user);
}