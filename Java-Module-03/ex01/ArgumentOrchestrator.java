public class ArgumentOrchestrator {
    private boolean isHenTurn = false; 

    public synchronized void printEgg() {
        while (isHenTurn) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Egg");
        isHenTurn = true;
        notify();
    }

    public synchronized void printHen() {
        while (!isHenTurn) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("Hen");
        isHenTurn = false;
        notify();
    }
}
