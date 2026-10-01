package fr._42.sockets.services;

import fr._42.sockets.models.User;

import java.util.Optional;

public interface UsersService {
    void            signUp(String username, String password);
    Optional<User>  signIn(String username, String password);
}
