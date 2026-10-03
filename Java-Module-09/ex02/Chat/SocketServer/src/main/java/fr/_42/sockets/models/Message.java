package fr._42.sockets.models;

import java.time.LocalDateTime;

public class Message {
    private Long            identifier;
    private Room            Room;
    private User            sender;
    private String          text;
    private LocalDateTime   sendingTime;

    public Message(
        Long identifier,
        Room room,
        User sender,
        String text,
        LocalDateTime sendingTime
    ) {
        this.identifier = identifier;
        this.Room = room;
        this.sender = sender;
        this.text = text;
        this.sendingTime = sendingTime;
    }
    public void setSendingTime(LocalDateTime sendingTime)   { this.sendingTime = sendingTime; }
    public void setIdentifier(Long identifier)              { this.identifier = identifier; }
    public void setSender(User sender)                      { this.sender = sender; }
    public void setText(String text)                        { this.text = text; }
    public void setRoom(Room room)                          { this.Room = room; }
    public LocalDateTime getSendingTime()                   { return sendingTime; }
    public Long getIdentifier()                             { return identifier; }
    public User getSender()                                 { return sender; }
    public String getText()                                 { return text; }
    public Room getRoom()                                   { return Room; }

    @Override
    public String toString() {
        return "Message{" +
            "identifier=" + identifier +
            ", Room=" + Room +
            ", sender=" + sender +
            ", text='" + text + '\'' +
            ", sendingTime=" + sendingTime +
            '}';
    }
}
