package _42.spring.service.services;

import _42.spring.service.models.User;
import _42.spring.service.repositories.UsersRepository;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    @Autowired
    public UsersServiceImpl(@Qualifier("usersRepositoryJdbcTemplate") UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public String signUp(String email) {
        try {
            Optional<User> existingUser = this.usersRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("User with email " + email + " already exists.");
            }

            String password = generateRandomString(8);
            User user = new User(
                null,
                email,
                password
            );

            this.usersRepository.save(user);
            return password;
        } catch (Exception e) {
            throw new RuntimeException("Failed to sign up user: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> getUser(String email) {
        return this.usersRepository.findByEmail(email);
    }

    private static String generateRandomString(int length) {
        String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz/*-+!@#$%^&()_[]{}|;:,.<>?";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(index));
        }
        
        return sb.toString();
    }
}