import java.util.Scanner;

public class Program {
    public static int iteration = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int nbr = scanner.nextInt();
        
        if (nbr <= 1) {
            System.err.println("IllegalArgument");
            System.exit(-1);
        }

        System.out.println(isPrime(nbr) + " " + iteration);
        scanner.close();
    }

    public static boolean isPrime(int n) {
        int i = 2;

        while (true) {

            iteration++;
            if (n % i == 0) return false;
            
            if (i * i > n) return true;
            i++;

        }
    }
}