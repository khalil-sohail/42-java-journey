import java.util.concurrent.ThreadLocalRandom;

public class ArrayGenerator {

    public static int[] generateArray(int size) {
        int[] array = new int[size];
        ThreadLocalRandom random = ThreadLocalRandom.current();
        
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(2001);
        }
        
        return array;
    }

    public static long calculateSequentialSum(int[] array) {
        long sum = 0;
        for (int val : array) {
            sum += val;
        }
        return sum;
    }
}
