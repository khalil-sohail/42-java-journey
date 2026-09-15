package fr._42.chat.app;

import java.util.Optional;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import fr._42.chat.models.Message;
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
            Optional<Message> messageOptional = messagesRepository.findById(2L);

            if (messageOptional.isEmpty()) {
                System.out.println("Message not found");
                return;
            }

            Message message = messageOptional.get();
            System.out.println("Before update:");
            System.out.println(message);

            message.setText("Hi");
            message.setDateTime(null);
            messagesRepository.update(message);
            Optional<Message> updatedMessageOptional = messagesRepository.findById(message.getId());

            if (updatedMessageOptional.isPresent()) {
                System.out.println("After update:");
                System.out.println(updatedMessageOptional.get());
            } else {
                System.out.println("Message not found after update");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}