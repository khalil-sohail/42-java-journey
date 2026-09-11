package fr._42.printer.app;

import fr._42.printer.logic.Logic;

public class Program {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Program <character 1> <character 2>");
            return;
        } else if (args[0].length() != 1 || args[1].length() != 1) {
            System.out.println("Error: The first two arguments must be single characters.");
            return;
        }

        try {
            char whiteChar = args[0].charAt(0);
            char blackChar = args[1].charAt(0);
            Logic logic = new Logic(whiteChar, blackChar);

            logic.convertImage();
            logic.printImage();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
    }
}
