package fr._42.sockets.server;

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
    private final ClientHandler clientHandler;

    @Autowired
    public Server(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
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
                clientHandler.handleClient(input, output);
            }
        } catch (IOException e) {
            throw new RuntimeException("Server error", e);
        }
    }
}