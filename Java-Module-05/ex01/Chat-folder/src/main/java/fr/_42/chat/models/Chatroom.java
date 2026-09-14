package fr._42.chat.models;

import java.util.List;

public class Chatroom {
    private Long id;
    private String name;
    private User owner;
    private List<Message> messages;

    public Chatroom() { }
    public Chatroom(
            Long id,
            String name,
            User owner,
            List<Message> messages
    ) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.messages = messages;
    }

    public Long             getId()         { return id; }
    public String           getName()       { return name; }
    public User             getOwner()      { return owner; }
    public List<Message>    getMessages()   { return messages; }
    
    public void setId(Long id)                      { this.id = id; }
    public void setName(String name)                { this.name = name; }
    public void setOwner(User owner)                { this.owner = owner; }
    public void setMessages(List<Message> messages) { this.messages = messages; }

    @Override
    public boolean equals(Object o) {
        if (this == o)                                  { return true; }
        if (o == null || getClass() != o.getClass())    { return false; }

        Chatroom chatroom = (Chatroom) o;
        return id != null && id.equals(chatroom.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Chatroom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
