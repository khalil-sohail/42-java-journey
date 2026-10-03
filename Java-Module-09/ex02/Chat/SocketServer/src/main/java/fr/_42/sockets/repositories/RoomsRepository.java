package fr._42.sockets.repositories;

import java.util.List;
import java.util.Optional;

import fr._42.sockets.models.Room;
import fr._42.sockets.models.User;

public interface RoomsRepository {
    Room            save(Room room);
    List<Room>      findAll();
    Optional<Room>  findById(Long roomId);
    void            addUserToRoom(User user, Room room);
    void            setLastRoom(User user, Room room);
    Optional<Room>  findLastRoom(User user);
}
