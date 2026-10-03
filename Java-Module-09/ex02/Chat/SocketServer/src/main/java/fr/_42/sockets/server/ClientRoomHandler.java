package fr._42.sockets.server;

import fr._42.sockets.server.Connection.JsonConnection;
import fr._42.sockets.server.Connection.protocols.*;
import fr._42.sockets.services.MessagesService;
import fr._42.sockets.services.RoomsService;
import fr._42.sockets.models.Message;
import fr._42.sockets.models.Room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.List;

import java.io.IOException;

@Component
public class ClientRoomHandler {
    private final MessagesService   messagesService;
    private final ClientRegistry    clientRegistry;
    private final RoomsService      roomsService;

    @Autowired
    public ClientRoomHandler(
        MessagesService messagesService,
        ClientRegistry clientRegistry,
        RoomsService roomsService
    ) {
        this.messagesService = messagesService;
        this.clientRegistry = clientRegistry;
        this.roomsService = roomsService;
    }

    public void handleRooms(ClientSession session) throws IOException {
        Optional<Room> lastRoom = roomsService.getLastRoom(session.getUser());
        if (lastRoom.isPresent()) {
            enterRoom(session, lastRoom.get());
        }

        while (true) {
            JsonConnection connection = session.getJsonConnection();
            connection.send(
                MessageType.ROOM_MENU,
                null
            );

            ProtocolMessage request = connection.receive();
            if (request == null) {
                return;
            }

            switch (request.type()) {
                case CREATE_ROOM -> {
                    createRoom(
                        session,
                        connection.getData(
                            request,
                            CreateRoomRequest.class
                        )
                    );
                }
                case ROOM_LIST_REQUEST -> {
                    chooseRoomFlow(session);
                }
                case EXIT -> {
                    return;
                }
                default -> {
                    connection.send(
                        MessageType.ERROR,
                        new TextResponse("Unknown message type: " + request.type())
                    );
                }
            }
        }
    }

    private void createRoom(
        ClientSession session,
        CreateRoomRequest request
    ) throws IOException {
        String name = request.name();
        if (name == null || name.isBlank()) {
            session.getJsonConnection().send(
                MessageType.ERROR,
                new TextResponse("Room name cannot be empty.")
            );
            return;
        }

        Room room = roomsService.createRoom(session.getUser(), name);
        session.getJsonConnection().send(
            MessageType.ROOM_CREATED,
            new RoomInfo(room.getIdentifier(), room.getName())
        );
    }

    private void sendRoomList(ClientSession session) throws IOException {
        List<RoomInfo> rooms = roomsService.getRooms()
            .stream()
            .map(room -> new RoomInfo(
                room.getIdentifier(),
                room.getName()
            ))
            .toList();

        session.getJsonConnection().send(
            MessageType.ROOM_LIST,
            new RoomListResponse(rooms)
        );
    }

    private void enterRoom(ClientSession session, Room room) throws IOException {
        session.setCurrentRoom(room);

        try {
            session.getJsonConnection().send(
                MessageType.ROOM_ENTERED,
                new RoomInfo(room.getIdentifier(), room.getName())
            );
            sendMessagesHistory(session, room);
            handleMessaging(session);
        } finally {
            session.setCurrentRoom(null);
        }
    }

    private void sendMessagesHistory(ClientSession session, Room room) throws IOException {
        List<Message> messages = messagesService.getLastMessages(
            room.getIdentifier(),
            30
        );

        List<ChatMessageResponse> history = messages.stream()
            .map(message ->
                new ChatMessageResponse(
                    room.getIdentifier(),
                    room.getName(),
                    message.getSender().getUsername(),
                    message.getText()
                )
            )
            .toList();

        session.getJsonConnection().send(
            MessageType.HISTORY,
            new HistoryResponse(
                room.getIdentifier(),
                room.getName(),
                history
            )
        );
    }

    private void chooseRoom(
        ClientSession session,
        ChooseRoomRequest request
    ) throws IOException {
        Optional<Room> room = roomsService.chooseRoom(
            session.getUser(),
            request.roomId()
        );

        if (room.isEmpty()) {
            session.getJsonConnection().send(
                MessageType.ERROR,
                new TextResponse(
                    "Room with ID "
                    + request.roomId()
                    + " does not exist."
                 )
            );
        } else {
            enterRoom(
                session,
                room.get()
            );
        }
    }

    private void chooseRoomFlow(ClientSession session) throws IOException {
        JsonConnection connection = session.getJsonConnection();
        sendRoomList(session);

        ProtocolMessage request = connection.receive();
        if (request == null) {
            return;
        } if (request.type() == MessageType.BACK) {
            return;
        } if (request.type() != MessageType.CHOOSE_ROOM) {
            connection.send(
                MessageType.ERROR,
                new TextResponse(
                    "Expected room selection."
                )
            );
            return;
        }

        ChooseRoomRequest chooseRequest = connection.getData(
            request,
            ChooseRoomRequest.class
        );

        chooseRoom(session, chooseRequest);
    }
    
    private void handleMessaging(ClientSession session) throws IOException {
        ProtocolMessage request;
        JsonConnection connection = session.getJsonConnection();

        while ((request = connection.receive()) != null) {
            switch (request.type()) {
                case EXIT_ROOM -> {
                    session.getJsonConnection().send(
                        MessageType.ROOM_LEAVE,
                        new RoomInfo(
                            session.getCurrentRoom().getIdentifier(),
                            session.getCurrentRoom().getName()
                        )
                    );
                    return;
                }
                case CHAT_MESSAGE -> {
                    ChatMessageRequest chatRequest = connection.getData(
                        request,
                        ChatMessageRequest.class
                    );
                    Room room = session.getCurrentRoom();

                    if (chatRequest.message() == null || chatRequest.message().isBlank()) {
                        connection.send(
                            MessageType.ERROR,
                            new TextResponse("Message cannot be empty.")
                        );
                        continue;
                    } if (chatRequest.roomId() == null || !room.getIdentifier().equals(chatRequest.roomId())) {
                        connection.send(
                            MessageType.ERROR,
                            new TextResponse("Invalid room for message.")
                        );
                        continue;
                    }

                    messagesService.sendMessage(
                        room,
                        session.getUser(),
                        chatRequest.message()
                    );

                    ChatMessageResponse response = new ChatMessageResponse(
                        room.getIdentifier(),
                        room.getName(),
                        session.getUser().getUsername(),
                        chatRequest.message()
                    );

                    clientRegistry.broadcastToRoom(
                        room,
                        MessageType.CHAT_MESSAGE,
                        response
                    );

                }
                default -> {
                    session.getJsonConnection().send(
                        MessageType.ERROR,
                        new TextResponse("Unknown message type: " + request.type())
                    );
                }
            }
        }
    }
}
