package fr._42.chat.app;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import fr._42.chat.models.Chatroom;
import fr._42.chat.models.Message;
import fr._42.chat.models.User;
import fr._42.chat.repositories.MessagesRepository;
import fr._42.chat.repositories.MessagesRepositoryJdbcImpl;

public class Program {

    public static void main(String[] args) {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:postgresql://localhost:5431/chat");
        config.setUsername("postgres");
        config.setPassword("password");

        try (HikariDataSource dataSource = new HikariDataSource(config)) {
            MessagesRepository messagesRepository = new MessagesRepositoryJdbcImpl(dataSource);
            User creator = new User(
                    1L,
                    "user1",
                    "password1",
                    new ArrayList<>(),
                    new ArrayList<>()
            );

            User author = creator;
            Chatroom room = new Chatroom(
                    1L,
                    "room1",
                    creator,
                    new ArrayList<>()
            );

            Message message = new Message(
                    null,
                    author,
                    room,
                    "Hi Mate, How areya !",
                    LocalDateTime.now()
            );

            System.out.println("Before save:");
            System.out.println("Message ID = " + message.getId());
            messagesRepository.save(message);
            System.out.println("After save:");
            System.out.println("Message ID = " + message.getId());
            System.out.println("Message = " + messagesRepository.findById(message.getId()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}