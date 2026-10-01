package _42.spring.service.models;

public class User {
    private Long    identifier;
    private String  email;
    private String  password;

    public User(Long identifier, String email, String password) {
        this.identifier = identifier;
        this.email = email;
        this.password = password;
    }

    public void     setIdentifier(Long identifier)  { this.identifier = identifier; }
    public void     setPassword(String password)    { this.password = password; }
    public void     setEmail(String email)          { this.email = email; }
    public Long     getIdentifier()                 { return identifier; }
    public String   getPassword()                   { return password; }
    public String   getEmail()                      { return email; }

    @Override
    public String toString() {
        return "User{" +
            "identifier=" + identifier +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
