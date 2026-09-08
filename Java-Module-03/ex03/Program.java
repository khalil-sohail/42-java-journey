import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Program {
    public static void main(String[] args) {
        int threadsCount = -1;

        if (args.length == 1 && args[0].startsWith("--threadsCount=")) {
            String countStr = args[0].substring("--threadsCount=".length());
            
            try {
                threadsCount = Integer.parseInt(countStr);
            } catch (NumberFormatException e) {
                System.err.println("Invalid threadsCount value: " + countStr);
                System.exit(1);
            } if (threadsCount <= 0) {
                System.err.println("threadsCount must be greater than 0");
                System.exit(1);
            }

            List<String> urls = readUrls("files_urls.txt");
            DownloadManager manager = new DownloadManager(urls);

            for (int i = 0; i < threadsCount; i++) {
                DownloadWorker worker = new DownloadWorker(manager);
                Thread thread = new Thread(worker, "Thread-" + (i + 1));
                thread.start();
            }
        } else {
            System.err.println("Missing or invalid --threadsCount argument");
            System.exit(1);
        }
            
    }

    private static List<String> readUrls(String inputPath) {
        List<String> urls = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(inputPath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                urls.add(line.trim());
            }
        } catch (IOException e) {
            System.err.print("I/O Error: " + e.getMessage() + ": ");
            System.exit(1);
        }

        return urls;
    }
}
