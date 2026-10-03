package fr._42.sockets.server;

import fr._42.sockets.services.MessagesService;
import fr._42.sockets.services.UsersService;
import fr._42.sockets.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Optional;
import java.net.Socket;

@Component
public class ClientHandler {
    private final MessagesService   messagesService;
    private final ClientRegistry    clientRegistry;
    private final UsersService      usersService;

    @Autowired
    public ClientHandler(MessagesService messagesService, ClientRegistry clientRegistry, UsersService usersService) {
        this.messagesService = messagesService;
        this.clientRegistry = clientRegistry;
        this.usersService = usersService;
    }

    public void handleClient(Socket socket) {
        try (
            socket;
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
        ) {
            handleProtocol(input, output);
        } catch (IOException e) {
            System.err.println("Client disconnected: " + e.getMessage());
        }
    }
    
    public void handleProtocol(BufferedReader input, PrintWriter output) throws IOException {
        output.println("Hello from Server!");
        String command = input.readLine();

        if ("signUp".equals(command)) {
            signUp(input, output);
        } else if ("signIn".equals(command)) {
            Optional<User> user = signIn(input, output);
            if (user.isEmpty()) {
                return;
            }

            handleMessaging(user.get(), input, output);
        } else {
            output.println("Unknown command: " + command);
        }
    }

    private void handleMessaging(User user, BufferedReader input, PrintWriter output) throws IOException {
        output.println("Start messaging");
        
        ClientSession session = new ClientSession(output, user);
        clientRegistry.add(session);
        try {
            String text;

            while ((text = input.readLine()) != null) {
                if ("Exit".equals(text)) {
                    output.println("You have left the chat.");
                    return;
                }
                
                messagesService.sendMessage(user, text);
                clientRegistry.broadcast(user.getUsername() + ": " + text);
            }
        } finally {
            clientRegistry.remove(session);
        }
    }

    private void signUp(BufferedReader input, PrintWriter output) throws IOException {
        output.println("Enter username:");
        String username = input.readLine();
        output.println("Enter password:");
        String password = input.readLine();

        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            output.println("Invalid credentials.");
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

        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            output.println("Invalid credentials.");
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
