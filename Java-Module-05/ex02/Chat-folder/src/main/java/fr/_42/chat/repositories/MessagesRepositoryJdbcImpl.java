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

    @Override
    public void save(Message message) {
        if (message.getAuthor() == null
                || message.getAuthor().getId() == null
                || message.getRoom() == null
                || message.getRoom().getId() == null) {
            throw new NotSavedSubEntityException("Author or chatroom is missing");
        }

        String checkAuthorSql = "SELECT id FROM users WHERE id = ?";
        String checkRoomSql = "SELECT id FROM chatrooms WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement checkAuthorStmt = connection.prepareStatement(checkAuthorSql);
            PreparedStatement checkRoomStmt = connection.prepareStatement(checkRoomSql)) {

            checkAuthorStmt.setLong(1, message.getAuthor().getId());
            try (ResultSet authorResult = checkAuthorStmt.executeQuery()) {
                if (!authorResult.next()) {
                    throw new NotSavedSubEntityException("Author does not exist in database");
                }
            }

            checkRoomStmt.setLong(1, message.getRoom().getId());
            try (ResultSet roomResult = checkRoomStmt.executeQuery()) {
                if (!roomResult.next()) {
                    throw new NotSavedSubEntityException("Chatroom does not exist in database");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String saveSql = """
            INSERT INTO messages (
                author_id,
                room_id,
                text,
                date_time
            )
            VALUES (?, ?, ?, ?)
        """;
        System.out.println();
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(saveSql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, message.getAuthor().getId());
            statement.setLong(2, message.getRoom().getId());
            statement.setString(3, message.getText());
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(message.getTrueDateTime()));

            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    message.setId(id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}






