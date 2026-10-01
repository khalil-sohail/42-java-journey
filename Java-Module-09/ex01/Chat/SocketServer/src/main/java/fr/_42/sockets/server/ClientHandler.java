package fr._42.sockets.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr._42.sockets.models.User;
import fr._42.sockets.services.MessagesService;
import fr._42.sockets.services.UsersService;

@Component
public class ClientHandler {
    private final MessagesService messagesService;
    private final UsersService usersService;

    @Autowired
    public ClientHandler(MessagesService messagesService, UsersService usersService) {
        this.messagesService = messagesService;
        this.usersService = usersService;
    }
    
    public void handleClient(
        BufferedReader input,
        PrintWriter output
    ) throws IOException {
        output.println("Hello from Server!");
        String command = input.readLine();

        if (command == null) {
            return;
        } else if ("signUp".equals(command)) {
            signUp(input, output);
        } else if ("signIn".equals(command)) {
            Optional<User> user = signIn(input, output);
            if (user.isEmpty()) {
                return;
            }

            handleMessaging(user.get(), input, output);
        }
    }

    private void handleMessaging(
        User user,
        BufferedReader input,
        PrintWriter output
    ) throws IOException {
        output.println("Start messaging");
        
        String text;
        while ((text = input.readLine()) != null) {
            if ("Exit".equals(text)) {
                output.println("You have left the chat.");
                return;
            }

            messagesService.sendMessage(user, text);
            // broadcast(user.getUsername() + ": " + text);
        }
    }

    private void signUp(BufferedReader input, PrintWriter output) throws IOException {
        output.println("Enter username:");
        String username = input.readLine();
        output.println("Enter password:");
        String password = input.readLine();

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return;
        }

        try {
            usersService.signUp(username, password);
            output.println("Successful!");
        } catch (RuntimeException e) {
            output.println(e.getMessage());
        }
    }

    private Optional<User> signIn(BufferedReader input, PrintWriter output) throws IOException {
        output.println("Enter username:");
        String username = input.readLine();
        output.println("Enter password:");
        String password = input.readLine();

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return Optional.empty();
        }

        try {
            Optional<User> u = usersService.signIn(username, password);

            if (u.isPresent()) {
                return u;
            } else {
                output.println("Invalid credentials.");
                return Optional.empty();
            }
        } catch (RuntimeException e) {
            output.println(e.getMessage());
            return Optional.empty();
        }
    }
}
