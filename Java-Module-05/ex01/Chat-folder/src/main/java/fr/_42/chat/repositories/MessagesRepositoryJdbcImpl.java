package fr._42.chat.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

import javax.sql.DataSource;

import fr._42.chat.models.Chatroom;
import fr._42.chat.models.Message;
import fr._42.chat.models.User;

public class MessagesRepositoryJdbcImpl implements MessagesRepository {
    private final DataSource dataSource;

    public MessagesRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public Optional<Message> findById(Long id) {
        String sql = """
            SELECT
                m.id AS message_id,
                m.text AS message_text,
                m.date_time AS message_date_time,
    
                u.id AS author_id,
                u.login AS author_login,
                u.password AS author_password,
    
                c.id AS room_id,
                c.name AS room_name
    
            FROM messages m
            LEFT JOIN users u
                ON m.author_id = u.id
            LEFT JOIN chatrooms c
                ON m.room_id = c.id
            WHERE m.id = ?
        """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return Optional.empty();
                }

                Long authorId = result.getObject("author_id", Long.class);
                User author = null;
                if (authorId != null) {
                    author = new User(
                            authorId,
                            result.getString("author_login"),
                            result.getString("author_password"),
                            null,
                            null
                    );
                }

                Long roomId = result.getObject("room_id", Long.class);
                Chatroom room = null;
                if (roomId != null) {
                    room = new Chatroom(
                            roomId,
                            result.getString("room_name"),
                            null,
                            null
                    );
                }

                Timestamp timestamp = result.getTimestamp("message_date_time");
                Message message = new Message(
                        result.getLong("message_id"),
                        author,
                        room,
                        result.getString("message_text"),
                        timestamp == null ? null : timestamp.toLocalDateTime()
                );

                return Optional.of(message);
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed getting message with id " + id,
                    e
            );
        }
    }
}