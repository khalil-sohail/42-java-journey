package fr._42.sockets.repositories;

import fr._42.sockets.models.Message;
import fr._42.sockets.models.Room;
import fr._42.sockets.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

import java.util.List;

@Component
public class MessagesRepositoryImpl implements MessagesRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Message> rowMapper = (rs, rowNum) -> {
        User sender = new User(
            rs.getLong("sender_id"),
            null, //rs.getLong("sender_last_room_id"),
            rs.getString("sender_username"),
            null
        );
        
        Room room = new Room(
            rs.getLong("room_id"),
            rs.getString("room_name")
        );

        return new Message(
            rs.getLong("message_id"),
            room,
            sender,
            rs.getString("text"),
            rs.getTimestamp("sending_time").toLocalDateTime()
        );
    };

    @Autowired 
    public MessagesRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(Message entity) {
        jdbcTemplate.update(
            """
            INSERT INTO messages_m09_ex02 (room_id, sender_id, text, sending_time)
            VALUES (?, ?, ?, ?)
            """,
            entity.getRoom().getIdentifier(),
            entity.getSender().getIdentifier(),
            entity.getText(),
            entity.getSendingTime()
        );
    }

    @Override
    public List<Message> findLastMessages(Long roomId, int count) {
        String sql = """
            SELECT *
            FROM (
                SELECT
                    m.id AS message_id,
                    m.text,
                    m.sending_time,

                    u.id AS sender_id,
                    u.last_room_id AS sender_last_room_id,
                    u.username AS sender_username,

                    r.id AS room_id,
                    r.name AS room_name

                FROM messages_m09_ex02 m

                JOIN users_m09_ex02 u
                    ON u.id = m.sender_id

                JOIN rooms_m09_ex02 r
                    ON r.id = m.room_id

                WHERE m.room_id = ?
                ORDER BY m.sending_time DESC
                LIMIT ?
            ) recent
            ORDER BY sending_time ASC;
        """;

        return jdbcTemplate.query(
            sql,
            rowMapper,
            roomId,
            count
        );
    }
}