package fr._42.chat.models;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

public class Message {
    private Long id;
    private User author;
    private Chatroom room;
    private String text;
    private LocalDateTime dateTime;

    public Message() { }
    public Message(
            Long id,
            User author,
            Chatroom room,
            String text,
            LocalDateTime dateTime
    ) {
        this.id = id;
        this.author = author;
        this.room = room;
        this.text = text;
        this.dateTime = dateTime;
    }

    public Long             getId()             { return id; }
    public User             getAuthor()         { return author; }
    public Chatroom         getRoom()           { return room; }
    public String           getText()           { return text; }
    public LocalDateTime    getTrueDateTime()   { return dateTime; }
    public String           getDateTime()       {
        if (dateTime == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");
        return dateTime.format(formatter);
    }

    public void setId(Long id)                      { this.id = id; }
    public void setAuthor(User author)              { this.author = author; }
    public void setRoom(Chatroom room)              { this.room = room; }
    public void setText(String text)                { this.text = text; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        Message message = (Message) o;
        return id != null && id.equals(message.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", author=" + author +
                ", room=" + room +
                ", text='" + text + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
