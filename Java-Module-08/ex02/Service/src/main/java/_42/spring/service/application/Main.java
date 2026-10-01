package _42.spring.service.application;

import _42.spring.service.config.ApplicationConfig;
import _42.spring.service.services.UsersService;
import _42.spring.service.models.User;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            ApplicationConfig.class
        )) {
            UsersService usersService = context.getBean(UsersService.class);
            String email = "khalil.sohail@example.com";
            
            String password = usersService.signUp(email);
            System.out.println("User signed up with password: " + password);

            Optional<User> user = usersService.getUser(email);
            if (user.isPresent()) {
                System.out.println("User retrieved: " + user.get());
            } else {
                System.out.println("User not found.");
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}