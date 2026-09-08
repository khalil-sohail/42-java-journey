
public class Program {
    private static int arraySize = -1;
    private static int threadsCount = -1;

    public static void main(String[] args) {

        if (
            args.length > 0 &&
            args[0].startsWith("--arraySize=") &&
            args[1].startsWith("--threadsCount=")
        ) {
            String countStr = args[0].substring("--arraySize=".length());
            String threadsCountStr = args[1].substring("--threadsCount=".length());

            try {
                arraySize = Integer.parseInt(countStr);
                threadsCount = Integer.parseInt(threadsCountStr);
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format for --arraySize or --threadsCount");
                System.exit(1);
            }
        } else {
            System.err.println("Missing or invalid argument");
            System.exit(1);
        }

        DisputeRunnable[] runnables = new DisputeRunnable[threadsCount];
        Thread[] threads = new Thread[threadsCount];
        int[] array = ArrayGenerator.generateArray(arraySize);
        long sequentialSum = ArrayGenerator.calculateSequentialSum(array);
        int chunkSize = (arraySize + threadsCount - 1) / threadsCount;
        
        System.out.println("Sum: " + sequentialSum);
        for (int i = 0; i < threadsCount; i++) {
            int start = i * chunkSize;
            int end = (i == threadsCount - 1) ? (arraySize - 1) : (start + chunkSize - 1);
            
            runnables[i] = new DisputeRunnable(array, i, start, end);
            threads[i] = new Thread(runnables[i]);
            
            threads[i].start();
        }

        long threadsTotalSum = 0;
        for (int i = 0; i < threadsCount; i++) {
            try {
                threads[i].join();
                threadsTotalSum += runnables[i].getSum();
            } catch (InterruptedException e) {
                System.err.println("Thread interrupted: " + e.getMessage());
            }
        }
		
        System.out.println("Sum by threads: " + threadsTotalSum);
    }
}