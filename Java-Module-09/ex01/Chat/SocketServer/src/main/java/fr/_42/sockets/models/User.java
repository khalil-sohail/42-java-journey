package fr._42.sockets.models;

public class User {
    private Long    identifier;
    private String  username;
    private String  password;


    public User(Long identifier, String username, String password) {
        this.identifier = identifier;
        this.username = username;
        this.password = password;
    }

    public void     setIdentifier(Long identifier)  { this.identifier = identifier; }
    public void     setPassword(String password)    { this.password = password; }
    public void     setUsername(String username)    { this.username = username; }
    public Long     getIdentifier()                 { return identifier; }
    public String   getPassword()                   { return password; }
    public String   getUsername()                   { return username; }

    @Override
    public String toString() {
        return "User{" +
            "identifier=" + identifier +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
