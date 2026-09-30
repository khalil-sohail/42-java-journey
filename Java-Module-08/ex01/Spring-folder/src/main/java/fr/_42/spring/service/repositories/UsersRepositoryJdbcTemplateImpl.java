package fr._42.spring.service.repositories;

import fr._42.spring.service.models.User;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

public class UsersRepositoryJdbcTemplateImpl implements UsersRepository {
    private final JdbcTemplate      jdbcTemplate;
    private final RowMapper<User>   userRowMapper = (resultSet, rowNum) ->
        new User(
            resultSet.getLong("id"),
            resultSet.getString("email")
        );

    public UsersRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User findById(Long id) {
        List<User> users = jdbcTemplate.query(
            "SELECT id, email FROM users WHERE id = ?",
            userRowMapper,
            id
        );

        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(
            "SELECT id, email FROM users",
            userRowMapper
        );
    }

    @Override
    public void save(User entity) {
        jdbcTemplate.update(
            "INSERT INTO users (email) VALUES (?)",
            entity.getEmail()
        );
    }

    @Override
    public void update(User entity) {
        Long id = jdbcTemplate.queryForObject(
            "INSERT INTO users (email) VALUES (?) RETURNING id",
            Long.class,
            entity.getEmail()
        );

        entity.setIdentifier(id);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(
            "DELETE FROM users WHERE id = ?",
            id
        );
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> users = jdbcTemplate.query(
            "SELECT id, email FROM users WHERE email = ?",
            userRowMapper,
            email
        );

        return users.stream().findFirst();
    }
}
