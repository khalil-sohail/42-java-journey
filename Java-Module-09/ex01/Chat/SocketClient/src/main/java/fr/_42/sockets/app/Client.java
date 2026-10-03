package fr._42.sockets.app;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void start(int port) {
        try (
            Socket          socket       = new Socket("localhost", port);
            BufferedReader  serverInput  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter     serverOutput = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader  consoleInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            if (authenticate(serverInput, serverOutput, consoleInput)) {
                startMessaging(serverInput, serverOutput, consoleInput);
            }

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    private static boolean authenticate(BufferedReader serverInput, PrintWriter serverOutput, BufferedReader consoleInput) throws IOException {
        String message;

        while ((message = serverInput.readLine()) != null) {
            System.out.println(message);

            if (
                message.equals("Hello from Server!")
                || message.equals("Enter username:")
                || message.equals("Enter password:")
            ) {
                String input = consoleInput.readLine();
                if (input == null) { return false; }
                serverOutput.println(input);
            } else if (message.equals("Start messaging")) {
                return true;
            } else if (message.equals("Invalid credentials.")) {
                return false;
            }
        }

        return false;
    }

    private static void startMessaging(BufferedReader serverInput, PrintWriter serverOutput, BufferedReader consoleInput) throws IOException {
        Thread.startVirtualThread(() -> {
            try {
                sendMessages(consoleInput, serverOutput);
            } catch (IOException e) {
                System.err.println(
                    "Failed to read console input: " + e.getMessage()
                );
            }
        });

        receiveMessages(serverInput);
    }

    private static void receiveMessages(BufferedReader serverInput) throws IOException {
        String message;

        while ((message = serverInput.readLine()) != null) {
            System.out.println(message);
        }
    }

    private static void sendMessages(BufferedReader consoleInput, PrintWriter serverOutput) throws IOException {
        String text;

        while ((text = consoleInput.readLine()) != null) {
            serverOutput.println(text);

            if ("Exit".equals(text)) {
                return;
            }
        }
    }
}
