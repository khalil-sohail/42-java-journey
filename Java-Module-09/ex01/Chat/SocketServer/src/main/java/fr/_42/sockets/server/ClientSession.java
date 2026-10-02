package fr._42.sockets.server;

import fr._42.sockets.models.User;

import java.io.PrintWriter;

public class ClientSession {
    private final PrintWriter   output;
    private final User          user;

    public ClientSession(PrintWriter output, User user) {
        this.output = output;
        this.user = user;
    }

    public PrintWriter  getOutput() { return output; }
    public User         getUser()   { return user; }

    public void send(String message) {
        synchronized (output) {
            output.println(message);
        }
    }
}
