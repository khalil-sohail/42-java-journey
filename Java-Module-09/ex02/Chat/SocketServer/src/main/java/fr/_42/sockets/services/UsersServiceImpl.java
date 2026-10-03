package fr._42.sockets.services;

import fr._42.sockets.repositories.UsersRepository;
import fr._42.sockets.models.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void signUp(String username, String password) {
        try {
            Optional<User> existingUser = this.usersRepository.findByUsername(username);
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("User with username " + username + " already exists.");
            }

            User user = new User(
                null,
                null,
                username,
                this.passwordEncoder.encode(password)
            );

            this.usersRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to sign up user: " + e.getMessage());
        }
    }

    @Override
    public Optional<User> signIn(String username, String password) {
        try {
            Optional<User> user = this.usersRepository.findByUsername(username);
            if (user.isPresent() && this.passwordEncoder.matches(password, user.get().getPassword())) {
                return user;
            } else {
                throw new IllegalArgumentException("Invalid username or password.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to sign in user: " + e.getMessage());
        }
    }
}