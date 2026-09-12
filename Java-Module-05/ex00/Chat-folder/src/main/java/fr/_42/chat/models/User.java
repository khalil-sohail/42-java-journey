package fr._42.chat.models;

import java.util.List;

public class User {
    private Long id;
    private String login;
    private String password;
    private List<Chatroom> createdRooms;
    private List<Chatroom> socializedRooms;

    public User() { }    
    public User(
        Long id,
        String login,
        String password,
        List<Chatroom> createdRooms,
        List<Chatroom> socializedRooms
    ) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.createdRooms = createdRooms;
        this.socializedRooms = socializedRooms;
    }

    public Long             getId()                 { return id; }
    public String           getLogin()              { return login; }
    public String           getPassword()           { return password; }
    public List<Chatroom>   getCreatedRooms()       { return createdRooms; }
    public List<Chatroom>   getSocializedRooms()    { return socializedRooms; }

    public void setId(Long userId)                                      { this.id = userId; }
    public void setLogin(String userLogin)                              { this.login = userLogin; }
    public void setPassword(String userPassword)                        { this.password = userPassword; }
    public void setCreatedRooms(List<Chatroom> userCreatedRooms)        { this.createdRooms = userCreatedRooms; }
    public void setSocializedRooms(List<Chatroom> userSocializedRooms)  { this.socializedRooms = userSocializedRooms; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return id != null ? id.equals(user.id) : user.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "User{ " +
                "id=`" + id +
                "`, login=`" + login +
                "` }";
    }
}
