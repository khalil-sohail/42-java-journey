package fr._42.services;

import fr._42.exceptions.AlreadyAuthenticatedException;
import fr._42.exceptions.EntityNotFoundException;
import fr._42.repositories.UsersRepository;
import fr._42.models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsersServiceImplTest {
    private UsersRepository usersRepository;
    private UsersServiceImpl usersService;

    @BeforeEach
    void init() {
        usersRepository = mock(UsersRepository.class);
        usersService = new UsersServiceImpl(usersRepository);
    }

    @Test
    void authenticateWithCorrectLoginAndPassword() {
        User user = new User(
            1L,
            "khalil",
            "1234",
            false
        );

        when(usersRepository.findByLogin("khalil")).thenReturn(user);
        boolean result = usersService.authenticate("khalil", "1234");

        assertTrue(result);
        assertTrue(user.isAuthenticated());
        verify(usersRepository).update(user);
    }

    @Test
    void authenticateWithIncorrectLogin() {
        when(usersRepository.findByLogin("unknown")).thenThrow(new EntityNotFoundException());
        assertThrows(
                EntityNotFoundException.class,
                () -> usersService.authenticate("unknown", "1234")
        );
        verify(usersRepository, never()).update(any());
    }

    @Test
    void authenticateWithIncorrectPassword() {
        User user = new User(
                1L,
                "khalil",
                "1234",
                false
        );

        when(usersRepository.findByLogin("khalil")).thenReturn(user);
        boolean result = usersService.authenticate("khalil", "wrong");

        assertFalse(result);
        assertFalse(user.isAuthenticated());
        verify(usersRepository, never()).update(user);
    }

    @Test
    void authenticateAlreadyAuthenticatedUser() {
        User user = new User(
            1L,
            "khalil",
            "1234",
            true
        );

        when(usersRepository.findByLogin("khalil")).thenReturn(user);
        assertThrows(
                AlreadyAuthenticatedException.class,
                () -> usersService.authenticate("khalil", "1234")
        );
        verify(usersRepository, never()).update(user);
    }
}
