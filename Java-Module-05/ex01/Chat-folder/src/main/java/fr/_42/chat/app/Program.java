package fr._42.chat.app;

import java.util.Optional;
import java.util.Scanner;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import fr._42.chat.repositories.MessagesRepository;
import fr._42.chat.repositories.MessagesRepositoryJdbcImpl;
import fr._42.chat.models.Message;

public class Program {

    public static void main(String[] args) {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:postgresql://localhost:5431/chat");
        config.setUsername("postgres");
        config.setPassword("password");

        try (HikariDataSource dataSource = new HikariDataSource(config);
            Scanner scanner = new Scanner(System.in)) {
            MessagesRepository messagesRepository = new MessagesRepositoryJdbcImpl(dataSource);

            System.out.println("Enter a message ID");
            System.out.print("-> ");

            Long id = scanner.nextLong();
            Optional<Message> message = messagesRepository.findById(id);

            if (message.isPresent()) {
                System.out.print("Message : ");
                System.out.print(
                    "{\n" +
                    "id=" + message.get().getId() + ",\n" +
                    "author=" + message.get().getAuthor() + ",\n" +
                    "room=" + message.get().getRoom() + ",\n" +
                    "Text=\"" + message.get().getText() + "\",\n" +
                    "dateTime=" + message.get().getDateTime() + ",\n" +
                    "}\n"
                );
            } else {
                System.out.println("Message not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
