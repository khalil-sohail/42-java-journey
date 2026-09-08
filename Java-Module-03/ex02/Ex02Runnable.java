
public class Ex02Runnable implements Runnable {
    private final int[]                 array;
    private final int                   threadId;
    private final int                   start;
    private final int                   end;

    private long sum = 0;

    public Ex02Runnable(int[] array, int threadId, int start, int end) {
        this.array = array;
        this.threadId = threadId;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        for (int i = start; i <= end; i++) {
            sum += array[i];
        }

        System.out.printf("Thread %d: from %d to %d sum is %d\n", threadId, start, end, sum);
    }
    
    public long getSum() {
        return sum;
    }
}
