import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.net.URI;

public class DownloadWorker implements Runnable {
    private final DownloadManager manager;

    public DownloadWorker(DownloadManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        while (true) {
            DownloadTask task = manager.getNextTask();
            if (task == null) {
                break;
            }
 
            System.out.println(
                Thread.currentThread().getName() + " start download file number " + task.getNumber()
            );

            try {
                download(task);
            } catch (IOException e) {
                System.err.println("Download failed: " + e.getMessage());
            }

            System.out.println(
                Thread.currentThread().getName() + " finish download file number " + task.getNumber()
            );
        }
    }

    private void download(DownloadTask task) throws IOException {
        URI uri = URI.create(task.getUrl());

        String fileName = Paths.get(uri.getPath()).getFileName().toString();
        try (InputStream input = uri.toURL().openStream()) {
            Files.copy(
                input,
                Paths.get(fileName),
                StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            System.err.println("Failed to download file from " + task.getUrl() + ": " + e.getMessage());
        }
    }
}
