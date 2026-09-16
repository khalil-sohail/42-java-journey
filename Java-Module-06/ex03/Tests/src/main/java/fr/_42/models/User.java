package fr._42.models;

public class User {
    private Long id;
    private String login;
    private String password;
    private boolean authenticated;

    public User(Long id, String login, String password, boolean authenticated) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.authenticated = authenticated;
    }

    public Long     getId()             { return id; }
    public String   getLogin()          { return login; }
    public String   getPassword()       { return password; }
    public boolean  isAuthenticated()   { return authenticated; }

    public void setAuthenticated(boolean authenticated) { this.authenticated = authenticated; }
}