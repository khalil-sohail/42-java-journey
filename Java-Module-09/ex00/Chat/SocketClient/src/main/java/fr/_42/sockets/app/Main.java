package fr._42.sockets.app;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class Main {
    public static void main(String[] args) {
        Args arguments = new Args();
        JCommander commander = JCommander.newBuilder().addObject(arguments).build();

        try {
            commander.parse(args);
            if (arguments.getPort() < 1024 || arguments.getPort() > 65535) {
                System.err.println("Error: Port number must be between 1024 and 65535.");
                return;
            }
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            commander.usage();
            return;
        }

        try (
            Socket socket               = new Socket("localhost", arguments.getPort());
            BufferedReader serverInput  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter serverOutput    = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            runClient(
                serverInput,
                serverOutput,
                consoleInput
            );
        } catch (IOException e) {
            System.err.println(
                "Connection error: " + e.getMessage()
            );
        }
    }

    private static void runClient(
        BufferedReader serverInput,
        PrintWriter serverOutput,
        BufferedReader consoleInput
    ) throws IOException {
        String message;

        while ((message = serverInput.readLine()) != null) {
            System.out.println(message);

            if (message.equals("Hello from Server!")
                || message.equals("Enter username:")
                || message.equals("Enter password:")) {
                String input = consoleInput.readLine();

                if (input == null) {
                    return;
                }

                serverOutput.println(input);
            }
        }
    }
}

