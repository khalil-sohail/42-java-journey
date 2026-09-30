package fr._42.spring.service.repositories;

import fr._42.spring.service.models.User;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsersRepositoryJdbcImpl implements UsersRepository {
    private final DataSource dataSource;

    public UsersRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private User mapUser(ResultSet resultSet) throws SQLException {
        return new User(
            resultSet.getLong("id"),
            resultSet.getString("email")
        );
    }

    @Override
    public User findById(Long id) {
        String sql = """
            SELECT
                id,
                email
            FROM users
            WHERE id = ?
        """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }

                return mapUser(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                "Failed getting user with id " + id,
                e
            );
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "SELECT id, email FROM users"
            );
            ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                users.add(mapUser(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    @Override
    public void save(User entity) {
        String sql = """
            INSERT INTO users (email)
            VALUES (?)
        """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entity.getEmail());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setIdentifier(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                "Failed saving user with email " + entity.getEmail(),
                e
            );
        }
    }

    @Override
    public void update(User entity) {
        String sql = """
            UPDATE users
            SET email = ?
            WHERE id = ?
        """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getEmail());
            statement.setLong(2, entity.getIdentifier());
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                "Failed updating user with id " + entity.getIdentifier(),
                e
            );
        }
    }

    @Override
    public void delete(Long id) {
        String sql = """
            DELETE FROM users
            WHERE id = ?
        """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                "Failed deleting user with id " + id,
                e
            );
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = """
            SELECT
                id,
                email
            FROM users
            WHERE email = ?
        """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                return Optional.of(mapUser(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                "Failed getting user with email " + email,
                e
            );
        }
    }
}
