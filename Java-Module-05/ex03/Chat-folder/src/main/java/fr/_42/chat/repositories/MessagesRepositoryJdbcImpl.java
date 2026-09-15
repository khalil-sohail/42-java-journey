package fr._42.chat.repositories;

import java.sql.*;
import java.util.Optional;

import javax.sql.DataSource;

import fr._42.chat.models.Chatroom;
import fr._42.chat.models.Message;
import fr._42.chat.models.User;
import fr._42.chat.models.NotSavedSubEntityException;

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

    @Override
    public void save(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("Message can't be null");
        } if (message.getAuthor() == null
                || message.getAuthor().getId() == null
                || message.getRoom() == null
                || message.getRoom().getId() == null) {
            throw new NotSavedSubEntityException("Author or chatroom is missing");
        }

        String checkAuthorSql = "SELECT id FROM users WHERE id = ?";
        String checkRoomSql = "SELECT id FROM chatrooms WHERE id = ?";
        String saveSql = """
            INSERT INTO messages (
                author_id,
                room_id,
                text,
                date_time
            )
            VALUES (?, ?, ?, ?)
        """;

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(checkAuthorSql)) {
                statement.setLong(
                        1,
                        message.getAuthor().getId()
                );

                try (ResultSet result = statement.executeQuery()) {
                    if (!result.next()) {
                        throw new NotSavedSubEntityException(
                                "Author does not exist in database"
                        );
                    }
                }
            }

            try (PreparedStatement statement = connection.prepareStatement(checkRoomSql)) {
                statement.setLong(
                        1,
                        message.getRoom().getId()
                );

                try (ResultSet result = statement.executeQuery()) {
                    if (!result.next()) {
                        throw new NotSavedSubEntityException(
                                "Chatroom does not exist in database"
                        );
                    }
                }
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    saveSql,
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setLong(
                        1,
                        message.getAuthor().getId()
                );

                statement.setLong(
                        2,
                        message.getRoom().getId()
                );

                statement.setString(
                        3,
                        message.getText()
                );

                if (message.getTrueDateTime() == null) {
                    statement.setNull(
                            4,
                            Types.TIMESTAMP
                    );
                } else {
                    statement.setTimestamp(
                            4,
                            Timestamp.valueOf(
                                    message.getTrueDateTime()
                            )
                    );
                }

                int affectedRows = statement.executeUpdate();
                if (affectedRows != 1) {
                    throw new RuntimeException(
                            "Failed to save message"
                    );
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (!generatedKeys.next()) {
                        throw new RuntimeException(
                                "Database did not return generated message ID"
                        );
                    }

                    message.setId(
                            generatedKeys.getLong(1)
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Database error while saving message",
                    e
            );
        }
    }

    @Override
    public void update(Message message) {
        if (message.getId() == null) {
            throw new IllegalArgumentException("Message ID can't be null");
        }

        String sql = """
            UPDATE messages
            SET author_id = ?,
                room_id = ?,
                text = ?,
                date_time = ?
            WHERE id = ?
        """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (message.getAuthor() == null || message.getAuthor().getId() == null) {
                statement.setNull(1, java.sql.Types.BIGINT);
            } else {
                statement.setLong(1, message.getAuthor().getId());
            }

            if (message.getRoom() == null || message.getRoom().getId() == null) {
                statement.setNull(2, java.sql.Types.BIGINT);
            } else {
                statement.setLong(2, message.getRoom().getId());
            }

            if (message.getText() == null) {
                statement.setNull(3, java.sql.Types.VARCHAR);
            } else {
                statement.setString(3, message.getText());
            }

            if (message.getTrueDateTime() == null) {
                statement.setNull(4, java.sql.Types.TIMESTAMP);
            } else {
                statement.setTimestamp(
                        4,
                        java.sql.Timestamp.valueOf(message.getTrueDateTime())
                );
            }

            statement.setLong(5, message.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException(
                        "Message with id " + message.getId() + " does not exist"
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while updating message", e);
        }
    }
}






