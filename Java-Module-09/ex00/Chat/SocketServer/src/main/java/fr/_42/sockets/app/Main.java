package fr._42.sockets.app;

import fr._42.sockets.config.ApplicationConfig;
import fr._42.sockets.server.Server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                ApplicationConfig.class
            )
        ) {
            Server server = context.getBean(Server.class);
            server.start(arguments.getPort());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return;
        }
    }
}

