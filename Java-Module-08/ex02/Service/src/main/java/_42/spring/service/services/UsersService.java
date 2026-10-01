package _42.spring.service.services;

import _42.spring.service.models.User;

import java.util.Optional;

public interface UsersService {
    String signUp(String email);
    Optional<User> getUser(String email);
}
