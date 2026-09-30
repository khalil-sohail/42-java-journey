package fr._42.spring.app;

import fr._42.spring.printer.Printer;
// import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        Printer printer =
            context.getBean(
                "printerWithPrefix",
                Printer.class
            );

        printer.print("Hello!");
        context.close();
    }
}