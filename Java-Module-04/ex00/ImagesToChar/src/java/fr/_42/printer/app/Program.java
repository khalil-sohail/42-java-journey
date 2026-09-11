package fr._42.printer.app;

import fr._42.printer.logic.Logic;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Program {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Program <character 1> <character 2> <image_absolute_path>");
            return;
        } else if (args[0].length() != 1 || args[1].length() != 1) {
            System.out.println("Error: The first two arguments must be single characters.");
            return;
        }

        char whiteChar = args[0].charAt(0);
        char blackChar = args[1].charAt(0);
        try {
            Path path = Paths.get(args[2]);
            if (!Files.exists(path) || !path.isAbsolute()) {
                System.out.println("Error: The third argument must be a valid image absolute path.");
                return;
            }

            Logic logic = new Logic(whiteChar, blackChar, path);

            logic.convertImage();
            logic.printImage();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
    }
}
