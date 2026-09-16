package fr._42.services;

import fr._42.models.User;
import fr._42.exceptions.AlreadyAuthenticatedException;
import fr._42.repositories.UsersRepository;

public class UsersServiceImpl {
    private final UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public boolean authenticate(String login, String password) {
        User user = usersRepository.findByLogin(login);

        if (user.isAuthenticated()) {
            throw new AlreadyAuthenticatedException();
        } if (!user.getPassword().equals(password)) {
            return false;
        }

        user.setAuthenticated(true);
        usersRepository.update(user);
        return true;
    }
}
