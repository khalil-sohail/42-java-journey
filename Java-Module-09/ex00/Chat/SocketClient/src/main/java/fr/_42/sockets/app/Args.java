package fr._42.sockets.app;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=")
public class Args {
    @Parameter(names = "--port", required = true)
    private int port;

    public int getPort() {
        return port;
    }
}
