package fr._42.sockets.app;

import com.beust.jcommander.ParameterException;
import com.beust.jcommander.JCommander;

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

        Client.start(arguments.getPort());
    }
}

