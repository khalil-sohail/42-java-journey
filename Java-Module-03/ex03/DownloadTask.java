public class DownloadTask {
    private final String url;
    private final int number;

    public DownloadTask(String url, int number) {
        this.url = url;
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public int getNumber() {
        return number;
    }
}
