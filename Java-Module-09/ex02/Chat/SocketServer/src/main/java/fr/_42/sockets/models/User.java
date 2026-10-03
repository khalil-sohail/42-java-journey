package fr._42.sockets.models;

public class User {
    private Long    identifier;
    private Long    lastRoomId;
    private String  username;
    private String  password;

    public User(Long identifier, Long lastRoomId, String username, String password) {
        this.identifier = identifier;
        this.lastRoomId = lastRoomId;
        this.username = username;
        this.password = password;
    }

    public void     setIdentifier(Long identifier)  { this.identifier = identifier; }
    public void     setLastRoomId(Long lastRoomId)  { this.lastRoomId = lastRoomId; }
    public void     setPassword(String password)    { this.password = password; }
    public void     setUsername(String username)    { this.username = username; }
    
    public Long     getIdentifier()                 { return identifier; }
    public Long     getLastRoomId()                 { return lastRoomId; }
    public String   getPassword()                   { return password; }
    public String   getUsername()                   { return username; }

    @Override
    public String toString() {
        return "User{" +
            "identifier=" + identifier +
            ", lastRoomId=" + lastRoomId +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
