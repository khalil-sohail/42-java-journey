package fr._42.sockets.protocols;

public enum MessageType {
    HELLO,

    SIGN_IN,
    SIGN_UP,
    AUTH_RESULT,

    ROOM_MENU,
    CREATE_ROOM,
    CHOOSE_ROOM,
    ROOM_LIST,
    ROOM_LIST_REQUEST,
    ROOM_CREATED,
    ROOM_ENTERED,
    ROOM_LEAVE,

    HISTORY,
    CHAT_MESSAGE,

    BACK,
    EXIT_ROOM,
    EXIT,

    INFO,
    ERROR
}
