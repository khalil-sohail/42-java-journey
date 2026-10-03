package fr._42.sockets.server;

import fr._42.sockets.services.UsersService;
import fr._42.sockets.models.User;
import fr._42.sockets.server.Connection.JsonConnection;
import fr._42.sockets.server.Connection.enums.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.net.Socket;

@Component
public class ClientHandler {
    private final ClientRegistry    clientRegistry;
    private final UsersService      usersService;
    private final ClientRoomHandler clientRoomHandler;

    @Autowired
    public ClientHandler(
        ClientRegistry clientRegistry,
        UsersService usersService,
        ClientRoomHandler clientRoomHandler
    ) {
        this.clientRegistry = clientRegistry;
        this.usersService = usersService;
        this.clientRoomHandler = clientRoomHandler;
    }

    public void handleClient(Socket socket) {
        try (
            socket;
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(
                socket.getOutputStream(),
                true
            )
        ) {
            JsonConnection connection = new JsonConnection(
                input,
                output
            );
            handleProtocol(connection);
        } catch (IOException e) {
            System.err.println("Client disconnected: " + e.getMessage());
        }
    }
    
    public void handleProtocol(JsonConnection connection) throws IOException {
        connection.send(
            MessageType.HELLO,
            new TextResponse("Hello from Server!")
        );

        ProtocolMessage request = connection.receive();
        if (request == null) {
            return;
        }

        switch (request.type()) {
            case SIGN_UP -> {
                signUp(
                    connection,
                    connection.getData(
                        request,
                        SignRequest.class
                    )
                );
            }
            case SIGN_IN -> {
                Optional<User> user = signIn(connection, connection.getData(
                    request,
                    SignRequest.class
                ));
                if (user.isEmpty()) {
                    return;
                }

                ClientSession session = new ClientSession(connection, user.get());
                clientRegistry.add(session);

                try {
                    clientRoomHandler.handleRooms(session);
                } finally {
                    clientRegistry.remove(session);
                }
            }
            case EXIT -> {
                return;
            }
            default -> {
                connection.send(
                    MessageType.AUTH_RESULT,
                    new StatusResponse(false, "Unknown message type: " + request.type())
                );
            }
        }
    }

    private void signUp(JsonConnection connection, SignRequest request) throws IOException {
        String username = request.username();
        String password = request.password();

        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            connection.send(
                MessageType.AUTH_RESULT,
                new StatusResponse(false, "Username and password cannot be empty.")
            );
            return;
        }

        try {
            usersService.signUp(username, password);
            connection.send(
                MessageType.AUTH_RESULT,
                new StatusResponse(true, "Successful!")
            );
        } catch (RuntimeException e) {
            connection.send(
                MessageType.AUTH_RESULT,
                new StatusResponse(false, e.getMessage())
            );
        }
    }

    private Optional<User> signIn(JsonConnection connection, SignRequest request) throws IOException {
        String username = request.username();
        String password = request.password();

        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            connection.send(
                MessageType.AUTH_RESULT,
                new StatusResponse(false, "Username and password cannot be empty.")
            );
            return Optional.empty();
        }

        try {
            Optional<User> u = usersService.signIn(username, password);

            if (u.isPresent()) {
                connection.send(
                    MessageType.AUTH_RESULT,
                    new StatusResponse(true, "Successful!")
                );
                return u;
            } else {
                connection.send(
                    MessageType.AUTH_RESULT,
                    new StatusResponse(false, "Invalid credentials.")
                );
                return Optional.empty();
            }
        } catch (RuntimeException e) {
            connection.send(
                MessageType.AUTH_RESULT,
                new StatusResponse(false, e.getMessage())
            );
            return Optional.empty();
        }
    }
}
