package fr._42.sockets.connection;

import fr._42.sockets.protocols.ProtocolMessage;
import fr._42.sockets.protocols.MessageType;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonConnection {
    private final BufferedReader    input;
    private final PrintWriter       output;
    private final ObjectMapper      objectMapper;

    public JsonConnection(BufferedReader input, PrintWriter output) {
        this.input = input;
        this.output = output;
        this.objectMapper = new ObjectMapper();
    }

    public void send(MessageType type, Object data) throws IOException {
        ProtocolMessage message = new ProtocolMessage(
            type,
            objectMapper.valueToTree(data)
        );

        synchronized (output) {
            output.println(
                objectMapper.writeValueAsString(message)
            );
        }
    }

    public ProtocolMessage receive() throws IOException {
        String line = input.readLine();
        if (line == null) {
            return null;
        }

        return objectMapper.readValue(
            line,
            ProtocolMessage.class
        );
    }

    public <T> T getData(ProtocolMessage message, Class<T> type) throws IOException {
        return objectMapper.treeToValue(
            message.data(),
            type
        );
    }
}
