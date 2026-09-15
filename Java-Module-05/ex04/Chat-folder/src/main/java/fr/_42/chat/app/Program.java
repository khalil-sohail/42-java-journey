package fr._42.chat.app;

import java.util.List;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import fr._42.chat.models.User;
import fr._42.chat.repositories.UsersRepository;
import fr._42.chat.repositories.UsersRepositoryJdbcImpl;

public class Program {
    public static void main(String[] args) {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl( "jdbc:postgresql://localhost:5431/chat");
        config.setUsername("postgres");
        config.setPassword("password");

        try (HikariDataSource dataSource = new HikariDataSource(config)) {
            UsersRepository usersRepository = new UsersRepositoryJdbcImpl(dataSource);

            int page = 1;
            int size = 5;
            List<User> users = usersRepository.findAll(page, size);
            
            System.out.println(
                "Page: " + page + ", size: " + size
            );

            for (User user : users) {
                System.out.println(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
