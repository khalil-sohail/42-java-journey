import java.util.List;

class DownloadManager {
    private final List<String> urls;
    private int taskIndex = 0;

    public DownloadManager(List<String> urls) {
        this.urls = urls;
    }

    public synchronized DownloadTask getNextTask() {
        if (taskIndex < urls.size()) {
            String url = urls.get(taskIndex);
            taskIndex++;
            return new DownloadTask(url, taskIndex);
        }

        return null;
    }
}