package fr._42.sockets.server;

import fr._42.sockets.services.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

@Component
public class Server {
    private final UsersService usersService;

    @Autowired
    public Server(UsersService usersService) {
        this.usersService = usersService;
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            try (
                Socket clientSocket = serverSocket.accept();
                BufferedReader input = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
                );
                PrintWriter output = new PrintWriter(
                    clientSocket.getOutputStream(),
                    true
                )
            ) {
                handleClient(input, output);
            }
        } catch (IOException e) {
            throw new RuntimeException("Server error", e);
        }
    }

    private void handleClient(BufferedReader input, PrintWriter output) throws IOException {
        output.println("Hello from Server!");
        String command = input.readLine();

        if ("signUp".equals(command)) {
            signUp(input, output);
        }
    }

    private void signUp(BufferedReader input, PrintWriter output) throws IOException {
        output.println("Enter username:");
        String username = input.readLine();

        output.println("Enter password:");
        String password = input.readLine();

        try {
            usersService.signUp(username, password);
            output.println("Successful!");
        } catch (RuntimeException e) {
            output.println(e.getMessage());
        }
    }
}