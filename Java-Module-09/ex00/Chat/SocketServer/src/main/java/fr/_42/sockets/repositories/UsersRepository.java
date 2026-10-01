package fr._42.sockets.repositories;

import java.util.Optional;

import fr._42.sockets.models.User;

public interface UsersRepository extends CrudRepository<User> {
    Optional<User> findByUsername(String username);
}
