package _42.spring.service.repositories;

import _42.spring.service.models.User;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("usersRepositoryJdbcTemplate")
public class UsersRepositoryJdbcTemplateImpl implements UsersRepository {
    private final JdbcTemplate      jdbcTemplate;
    private final RowMapper<User>   userRowMapper = (resultSet, rowNum) -> new User(
        resultSet.getLong("id"),
        resultSet.getString("email"),
        resultSet.getString("password")
    );

    @Autowired
    public UsersRepositoryJdbcTemplateImpl(@Qualifier("hikariDataSource") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User findById(Long id) {
        List<User> users = jdbcTemplate.query(
            "SELECT id, email, password FROM users_ex02 WHERE id = ?",
            userRowMapper,
            id
        );

        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(
            "SELECT id, email, password FROM users_ex02",
            userRowMapper
        );
    }

    @Override
    public void save(User entity) {
        jdbcTemplate.update(
            "INSERT INTO users_ex02 (email, password) VALUES (?, ?)",
            entity.getEmail(),
            entity.getPassword()
        );
    }

    @Override
    public void update(User entity) {
        Long id = jdbcTemplate.queryForObject(
            "INSERT INTO users_ex02 (email, password) VALUES (?, ?) RETURNING id",
            Long.class,
            entity.getEmail(),
            entity.getPassword()
        );

        entity.setIdentifier(id);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(
            "DELETE FROM users_ex02 WHERE id = ?",
            id
        );
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> users = jdbcTemplate.query(
            "SELECT id, email, password FROM users_ex02 WHERE email = ?",
            userRowMapper,
            email
        );

        return users.stream().findFirst();
    }
}
