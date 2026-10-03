package fr._42.sockets.services;

import fr._42.sockets.repositories.RoomsRepository;
import fr._42.sockets.models.User;
import fr._42.sockets.models.Room;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.List;

@Component
public class RoomsServiceImpl implements RoomsService {
    private final RoomsRepository roomRepository;

    @Autowired
    public RoomsServiceImpl(RoomsRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    
    @Override
    public Room createRoom(User user, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Room name cannot be empty.");
        }

        Room room = new Room(null, name);
        room = roomRepository.save(room);
        return room;
    }

    @Override
    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<Room> chooseRoom(User user, Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);

        room.ifPresent(value -> {
            roomRepository.addUserToRoom(user, value);
            roomRepository.setLastRoom(user, value);
        });

        return room;
    }

    @Override
    public Optional<Room> getLastRoom(User user) {
        return roomRepository.findLastRoom(user);
    }

}
