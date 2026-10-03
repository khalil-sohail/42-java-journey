package fr._42.sockets.repositories;

import fr._42.sockets.models.User;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component()
public class UsersRepositoryImpl implements UsersRepository {
    private final JdbcTemplate      jdbcTemplate;
    private final RowMapper<User>   userRowMapper = (resultSet, rowNum) -> new User(
        resultSet.getLong("id"),
        resultSet.getLong("last_room_id"),
        resultSet.getString("username"),
        resultSet.getString("password")
    );

    @Autowired
    public UsersRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User findById(Long id) {
        List<User> users = jdbcTemplate.query(
            "SELECT id, last_room_id, username, password FROM users_m09_ex02 WHERE id = ?",
            userRowMapper,
            id
        );

        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(
            "SELECT id, last_room_id, username, password FROM users_m09_ex02",
            userRowMapper
        );
    }

    @Override
    public void save(User entity) {
        jdbcTemplate.update(
            "INSERT INTO users_m09_ex02 (username, password) VALUES (?, ?)",
            entity.getUsername(),
            entity.getPassword()
        );
    }

    @Override
    public void update(User entity) {
        Long id = jdbcTemplate.queryForObject(
            "INSERT INTO users_m09_ex02 (last_room_id, username, password) VALUES (?, ?) RETURNING id",
            Long.class,
            entity.getLastRoomId(),
            entity.getUsername(),
            entity.getPassword()
        );

        entity.setIdentifier(id);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(
            "DELETE FROM users_m09_ex02 WHERE id = ?",
            id
        );
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> users = jdbcTemplate.query(
            "SELECT id, last_room_id, username, password FROM users_m09_ex02 WHERE username = ?",
            userRowMapper,
            username
        );

        return users.stream().findFirst();
    }
}
