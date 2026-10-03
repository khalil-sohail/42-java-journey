package fr._42.sockets.app;

import fr._42.sockets.connection.JsonConnection;
import fr._42.sockets.protocols.*;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private static JsonConnection connection;

    public static void start(int port) {
        try (
            Socket          socket       = new Socket("localhost", port);
            BufferedReader  serverInput  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter     serverOutput = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader  consoleInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            connection = new JsonConnection(serverInput, serverOutput);
            if (authenticate(consoleInput)) {
                receiveMessages(consoleInput);
            }

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    private static boolean authenticate(BufferedReader consoleInput) throws IOException {
        MessageType     authAction = null;
        ProtocolMessage request;

        while ((request = connection.receive()) != null) {
            switch (request.type()) {
                case HELLO -> {
                    TextResponse hello = connection.getData(request, TextResponse.class);
                    System.out.println(hello.message());
                    System.out.println("1. signIn");
                    System.out.println("2. signUp");
                    System.out.println("3. Exit");

                    String input = getInput(consoleInput, "> ");
                    if (input.equals("1")) {
                        authAction = MessageType.SIGN_IN;
                    } else if (input.equals("2")) {
                        authAction = MessageType.SIGN_UP;
                    } else if (input.equals("3")) {
                        connection.send(MessageType.EXIT, null);
                        return false;
                    } else {
                        System.err.println("Invalid input.");
                        return false;
                    }

                    String username = getInput(consoleInput, "> Enter username: ");
                    String password = getInput(consoleInput, "> Enter password: ");
                    connection.send(authAction, new SignRequest(username, password));
                }
                case AUTH_RESULT -> {
                    StatusResponse authStatus = connection.getData(request, StatusResponse.class);
                    System.out.println(authStatus.message());
                    return authAction == MessageType.SIGN_IN && authStatus.success();
                }
                default -> {
                    System.err.println("Unexpected message type: " + request.type());
                    return false;
                }
            }
        }

        return false;
    }

    private static void receiveMessages(BufferedReader consoleInput) throws IOException {
        RoomInfo currentRoom = null;
        Thread chatSenderThread = null;

        ProtocolMessage request;
        while ((request = connection.receive()) != null) {
            switch (request.type()) {

                case ROOM_MENU    -> {
                    while (true) {
                        System.out.println("1. Create Room");
                        System.out.println("2. Choose Room");
                        System.out.println("3. Exit");

                        String input = getInput(consoleInput, "> ");
                        if (input.equals("1")) {
                            String roomName;
                            roomName = getInput(consoleInput, "> Enter room name: ");
                            connection.send(MessageType.CREATE_ROOM, new CreateRoomRequest(roomName));

                            break;
                        } if (input.equals("2")) {
                            connection.send(MessageType.ROOM_LIST_REQUEST, null);

                            break;
                        } if (input.equals("3")) {
                            connection.send(MessageType.EXIT, null);

                            return;
                        }

                        System.err.println("Invalid input.");
                    }
                }

                case ROOM_LIST -> {
                    RoomListResponse roomList = connection.getData(
                        request,
                        RoomListResponse.class
                    );

                    if (roomList.rooms().isEmpty()) {
                        System.out.println("No rooms available, press Enter to return to the main menu...");
                        consoleInput.readLine();
                        connection.send(MessageType.BACK, null);
                        continue;
                    }

                    System.out.println("Rooms:");
                    for (RoomInfo room : roomList.rooms()) {
                        System.out.println(room.id() + ". Name: " + room.name());
                    }
                    System.out.println("\n0. Back");

                    while (true) {
                        String input = getInput(consoleInput, "> ");
                        if (input.equals("0")) {
                            connection.send(MessageType.BACK, null);
                            break;
                        }

                        try {
                            long roomId = Long.parseLong(input);
                            connection.send(MessageType.CHOOSE_ROOM, new ChooseRoomRequest(roomId));
                            break;
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid input.");
                        }
                    }
                }

                case HISTORY -> {
                    HistoryResponse history = connection.getData(
                        request,
                        HistoryResponse.class
                    );

                    System.out.println(history.roomName() + " ------");
                    for (ChatMessageResponse message : history.messages()) {
                        System.out.println(message.username() + ": " + message.message());
                    }

                    if (currentRoom == null) {
                        System.err.println("Received history without an active room.");
                        continue;
                    }

                    RoomInfo room = currentRoom;
                    chatSenderThread = Thread.startVirtualThread(() -> {
                        try {
                            sendMessages(consoleInput, room);
                        } catch (IOException e) {
                            System.err.println("Failed to read console input: " + e.getMessage());
                        }
                    });
                }

                case CHAT_MESSAGE -> {
                    ChatMessageResponse chatMessage =connection.getData(
                        request,
                        ChatMessageResponse.class
                    );

                    System.out.println(chatMessage.username() + ": " + chatMessage.message());
                }

                case ROOM_LEAVE -> {
                    RoomInfo room = connection.getData(
                        request,
                        RoomInfo.class
                    );

                    if (chatSenderThread != null) {
                        try {
                            chatSenderThread.join();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }

                        chatSenderThread = null;
                    }

                    currentRoom = null;
                    System.out.println("You have left " + room.name());
                }

                case ROOM_CREATED -> {
                    RoomInfo room = connection.getData(request, RoomInfo.class);
                    System.out.println("Room created: " + room.name());
                }

                case ROOM_ENTERED -> {
                    currentRoom = connection.getData(request, RoomInfo.class);
                }

                case INFO -> {
                    TextResponse info = connection.getData(request, TextResponse.class);
                    System.out.println(info.message());
                }

                case ERROR -> {
                    TextResponse error = connection.getData(request, TextResponse.class);
                    System.err.println(error.message());
                }

                default -> {
                    System.err.println("Unexpected message type: " + request.type());
                }
            }
        }
    }

    private static void sendMessages(BufferedReader consoleInput, RoomInfo room) throws IOException {
        while (true) {
            String input = getInput(consoleInput, "> ");

            if (input.equalsIgnoreCase("Exit")) {
                connection.send(MessageType.EXIT_ROOM, null);
                return;
            }

            connection.send(MessageType.CHAT_MESSAGE, new ChatMessageRequest(room.id(), input));
        }
    }

    private static String getInput(BufferedReader consoleInput, String prompt) throws IOException {    
        do {
            System.out.print(prompt);
            String input = consoleInput.readLine();
            if (input == null || input.isBlank()) {
                System.err.println("\nInput cannot be blank.");
            } else {
                return input;
            }
        } while (true);
    }
}
