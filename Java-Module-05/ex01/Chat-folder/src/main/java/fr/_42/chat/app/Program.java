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

        config.setJdbcUrl("jdbc:postgresql://localhost:5432/chat");
        config.setUsername("postgres");
        config.setPassword("password");

        try (HikariDataSource dataSource = new HikariDataSource(config);
             Scanner scanner = new Scanner(System.in)) {

            MessagesRepository messagesRepository =
                    new MessagesRepositoryJdbcImpl(dataSource);

            System.out.println("Enter a message ID");
            System.out.print("-> ");

            Long id = scanner.nextLong();

            Optional<Message> message =
                    messagesRepository.findById(id);

            if (message.isPresent()) {
                System.out.println("Message : ");
                System.out.println(message.get());
            } else {
                System.out.println("Message not found");
            }
        }
    }
}
