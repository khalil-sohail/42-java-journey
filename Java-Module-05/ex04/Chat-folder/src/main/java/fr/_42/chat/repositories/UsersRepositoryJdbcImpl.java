package fr._42.chat.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import fr._42.chat.models.Chatroom;
import fr._42.chat.models.User;

public class UsersRepositoryJdbcImpl implements UsersRepository {

    private final DataSource dataSource;

    public UsersRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> findAll(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException(
                "Page must be >= 0 and size must be > 0"
            );
        }

        String sql = """
            WITH paged_users AS (
                SELECT
                    id,
                    login,
                    password
                FROM users
                ORDER BY id
                LIMIT ?
                OFFSET ?
            )

            SELECT
                u.id AS user_id,
                u.login AS user_login,
                u.password AS user_password,

                created.id AS created_room_id,
                created.name AS created_room_name,

                socialized.id AS socialized_room_id,
                socialized.name AS socialized_room_name

            FROM paged_users u

            LEFT JOIN chatrooms created
                ON created.owner_id = u.id

            LEFT JOIN users_chatrooms uc
                ON uc.user_id = u.id

            LEFT JOIN chatrooms socialized
                ON socialized.id = uc.room_id

            ORDER BY
                u.id,
                created.id,
                socialized.id
            """;

        Map<Long, User> users = new LinkedHashMap<>();

        Map<Long, Set<Long>> createdRoomIds = new HashMap<>();
        Map<Long, Set<Long>> socializedRoomIds = new HashMap<>();

        int offset = page * size;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                 connection.prepareStatement(sql)) {

            statement.setInt(1, size);
            statement.setInt(2, offset);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {
                    Long userId = result.getLong("user_id");

                    User user = users.get(userId);

                    if (user == null) {
                        user = new User(
                            userId,
                            result.getString("user_login"),
                            result.getString("user_password"),
                            new ArrayList<>(),
                            new ArrayList<>()
                        );

                        users.put(userId, user);

                        createdRoomIds.put(
                            userId,
                            new HashSet<>()
                        );

                        socializedRoomIds.put(
                            userId,
                            new HashSet<>()
                        );
                    }

                    Long createdRoomId = result.getObject(
                        "created_room_id",
                        Long.class
                    );

                    if (createdRoomId != null
                            && createdRoomIds
                                .get(userId)
                                .add(createdRoomId)) {

                        Chatroom createdRoom = new Chatroom(
                            createdRoomId,
                            result.getString("created_room_name"),
                            null,
                            new ArrayList<>()
                        );

                        user.getCreatedRooms().add(createdRoom);
                    }

                    Long socializedRoomId = result.getObject(
                        "socialized_room_id",
                        Long.class
                    );

                    if (socializedRoomId != null
                            && socializedRoomIds
                                .get(userId)
                                .add(socializedRoomId)) {

                        Chatroom socializedRoom = new Chatroom(
                            socializedRoomId,
                            result.getString("socialized_room_name"),
                            null,
                            new ArrayList<>()
                        );

                        user.getSocializedRooms()
                            .add(socializedRoom);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                "Failed getting users",
                e
            );
        }

        return new ArrayList<>(users.values());
    }
}
