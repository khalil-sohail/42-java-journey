package fr._42.sockets.server;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class Server {
    private final ClientHandler clientHandler;

    public Server(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void start(int port) {
        try (
            ServerSocket    serverSocket = new ServerSocket(port);
            ExecutorService executor     = Executors.newVirtualThreadPerTaskExecutor()
        ) {
            while (true) {
                Socket clientSocket = serverSocket.accept();

                executor.submit(() -> {
                    try {
                        clientHandler.handleClient(clientSocket);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException("Server error", e);
        }
    }
}
