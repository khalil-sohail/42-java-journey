package fr._42.sockets.repositories;

import fr._42.sockets.models.Room;
import fr._42.sockets.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.List;

import javax.sql.DataSource;

@Component
public class RoomsRepositoryImpl implements RoomsRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Room> rowMapper = (rs, rowNum) -> new Room(
        rs.getLong("id"),
        rs.getString("name")
    );

    @Autowired
    public RoomsRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Room save(Room room) {
        Long id = jdbcTemplate.queryForObject(
            """
            INSERT INTO rooms_m09_ex02 (name)
            VALUES (?)
            RETURNING id
            """,
            Long.class,
            room.getName()
        );

        room.setIdentifier(id);
        return room;
    }

    @Override
    public List<Room> findAll() {
        return jdbcTemplate.query(
            """
            SELECT id, name
            FROM rooms_m09_ex02
            ORDER BY id
            """,
            rowMapper
        );
    }

    @Override
    public Optional<Room> findById(Long roomId) {
        List<Room> rooms = jdbcTemplate.query(
            """
            SELECT id, name
            FROM rooms_m09_ex02
            WHERE id = ?
            """,
            rowMapper,
            roomId
        );

        return rooms.stream().findFirst();
    }

    @Override
    public void addUserToRoom(User user, Room room) {
        jdbcTemplate.update(
            """
            INSERT INTO users_rooms_m09_ex02 (user_id, room_id)
            VALUES (?, ?)
            ON CONFLICT (user_id, room_id) DO NOTHING
            """,
            user.getIdentifier(),
            room.getIdentifier()
        );
    }

    @Override
    public void setLastRoom(User user, Room room) {
        jdbcTemplate.update(
            """
            UPDATE users_m09_ex02
            SET last_room_id = ?
            WHERE id = ?
            """,
            room.getIdentifier(),
            user.getIdentifier()
        );
    }

    @Override
    public Optional<Room> findLastRoom(User user) {
        List<Room> rooms = jdbcTemplate.query(
            """
            SELECT r.id, r.name
            FROM users_m09_ex02 u
            JOIN rooms_m09_ex02 r
                ON r.id = u.last_room_id
            WHERE u.id = ?
            """,
            rowMapper,
            user.getIdentifier()
        );

        return rooms.stream().findFirst();
    }
}

