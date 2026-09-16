package fr._42.repositories;

import fr._42.models.User;

public interface UsersRepository {
    User findByLogin(String login);
    void update(User user);
}
