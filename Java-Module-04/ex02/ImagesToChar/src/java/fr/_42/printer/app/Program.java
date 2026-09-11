package fr._42.printer.app;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import fr._42.printer.logic.Logic;

public class Program {
    public static void main(String[] args) {
        Args arguments = new Args();

        JCommander commander = JCommander.newBuilder()
                .addObject(arguments)
                .build();

        try {
            commander.parse(args);
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
            commander.usage();
            return;
        }

        try {
            Logic logic = new Logic(
                arguments.getWhite(),
                arguments.getBlack()
            );

            logic.printImage();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
    }
}

