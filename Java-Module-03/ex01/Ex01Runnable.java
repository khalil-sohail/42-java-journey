
public class Ex01Runnable implements Runnable {
    private final ArgumentOrchestrator  presenter;
    private final String                name;
    private final int                   count;

    public Ex01Runnable(String name, int count, ArgumentOrchestrator presenter) {
        this.presenter = presenter;
        this.count = count;
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 0; i < count; i++) {
            if (name.equals("Egg")) { presenter.printEgg(); }
            else                    { presenter.printHen(); }
        }
    }

}
