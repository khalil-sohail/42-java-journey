package fr._42.chat.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            JOIN users u
                ON m.author_id = u.id
            JOIN chatrooms c
                ON m.room_id = c.id
            WHERE m.id = ?
        """;

        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) { return Optional.empty(); }

                User author = new User(
                        result.getLong("author_id"),
                        result.getString("author_login"),
                        result.getString("author_password"),
                        null,
                        null
                );

                Chatroom room = new Chatroom(
                        result.getLong("room_id"),
                        result.getString("room_name"),
                        null,
                        null
                );

                Message message = new Message(
                        result.getLong("message_id"),
                        author,
                        room,
                        result.getString("message_text"),
                        result.getTimestamp("message_date_time").toLocalDateTime()
                );

                return Optional.of(message);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}