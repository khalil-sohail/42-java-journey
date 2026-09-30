package fr._42.spring.service.application;

import fr._42.spring.service.repositories.UsersRepository;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr._42.spring.service.models.User;

public class Main {
    public static void main(String[] args) {
        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml")) {

            UsersRepository usersRepository = context.getBean(
                "usersRepositoryJdbc",
                UsersRepository.class
            );
            for (User user : usersRepository.findAll()) {
                System.out.println(user);
            }

            System.out.println("--------------------------------------------------");

            usersRepository = context.getBean(
                "usersRepositoryJdbcTemplate",
                UsersRepository.class
            );
            for (User user : usersRepository.findAll()) {
                System.out.println(user);
            }
        }
    }
}
