
public class Ex00Runnable implements Runnable {
    private final String    name;
    private final int       count;

    public Ex00Runnable(String name, int count) {
        this.name = name;
        this.count = count;
    }

    @Override
    public void run() {
        for (int i = 0; i < this.count; i++) {
            System.out.println(this.name);
        }
    }
}
