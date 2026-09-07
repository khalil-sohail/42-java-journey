import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.stream.Stream;

public class Program {

    private static Path currentPath;

    public static void main(String[] args) {
        if (args.length != 1 || !args[0].startsWith("--current-folder=")) {
            System.err.println("Error: Please provide starting folder argument exactly like: --current-folder=/absolute/path");
            System.exit(1);
        }

        String startingPathStr = args[0].substring("--current-folder=".length());
        currentPath = Paths.get(startingPathStr).toAbsolutePath().normalize();

        if (!Files.exists(currentPath)) {
            System.err.println("Error: Directory '" + currentPath + "' does not exist.");
            System.exit(1);
        } else if (!Files.isDirectory(currentPath)) {
            System.err.println("Error: '" + currentPath + "' is not a directory.");
            System.exit(1);
        }

        System.out.println(currentPath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) { continue; }
                if (line.equals("exit")) { break; }

                executeCommand(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading user input: " + e.getMessage());
        }
    }

    private static void executeCommand(String line) {
        String[] tokens = line.split("\\s+");
        String command = tokens[0];

        try {
            switch (command) {
                case "ls":
                    if (tokens.length > 1) {
                        System.out.println("Usage: ls");
                    } else { handleLs(); }
                    break;

                case "cd":
                    if (tokens.length != 2) { System.out.println("Usage: cd <folder_name>");
                    } else { handleCd(tokens[1]); }
                    break;

                case "mv":
                    if (tokens.length != 3) {
                        System.out.println("Usage: mv <source> <target>");
                    } else { handleMv(tokens[1], tokens[2]); }
                    break;

                default:
                    System.out.println("Unknown command: " + command);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error executing command: " + e.getMessage());
        }
    }

    private static void handleLs() throws IOException {
        try (Stream<Path> stream = Files.list(currentPath)) {
            stream.forEach(path -> {
                try {
                    long sizeInBytes = 0;
                    if (Files.isDirectory(path)) {
                        sizeInBytes = calculateFolderSize(path);
                    } else {
                        sizeInBytes = Files.size(path);
                    }
                    long sizeInKB = sizeInBytes / 1024;
                    System.out.println(path.getFileName() + " " + sizeInKB + " KB");
                } catch (IOException e) {
                    System.out.println(path.getFileName() + " Size Unknown (Error: " + e.getMessage() + ")");
                }
            });
        }
    }

    private static void handleCd(String targetStr) {
        Path targetPath = currentPath.resolve(targetStr).normalize();

        if (!Files.exists(targetPath)) {
            System.out.println("cd: no such file or directory: " + targetStr);
            return;
        } else if (!Files.isDirectory(targetPath)) {
            System.out.println("cd: not a directory: " + targetStr);
            return;
        }

        currentPath = targetPath;
        System.out.println(currentPath);
    }

    private static void handleMv(String sourceStr, String targetStr) throws IOException {
        Path sourcePath = currentPath.resolve(sourceStr).normalize();
        if (!Files.exists(sourcePath)) {
            System.out.println("mv: no such file or directory: " + sourceStr);
            return;
        }

        Path targetPath = currentPath.resolve(targetStr).normalize();
        if (Files.isDirectory(targetPath)) {
            targetPath = targetPath.resolve(sourcePath.getFileName());
        }

        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    private static long calculateFolderSize(Path folderPath) throws IOException {
        try (Stream<Path> walk = Files.walk(folderPath)) {
            return walk.filter(Files::isRegularFile)
                       .mapToLong(p -> {
                            try { return Files.size(p); }
                            catch (IOException e) { return 0L; }
                       })
                       .sum();
        }
    }
}
