package fr._42.sockets.models;

public class Room {
    private Long    identifier;
    private String  name;

    public Room(Long identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public void         setIdentifier(Long identifier)  { this.identifier = identifier; }
    public void         setName(String name)            { this.name = name; }
    public Long         getIdentifier()                 { return identifier; }
    public String       getName()                       { return name; }

    @Override
    public String toString() {
        return "Room{" +
            "id=" + identifier +
            ", name='" + name + '\'' +
            '}';
    }
}
