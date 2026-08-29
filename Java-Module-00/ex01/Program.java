import java.util.Scanner;

public class Program {
    public static int iteration = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("-> ");
        int nbr = scanner.nextInt();
        
        if (nbr <= 1) {
            System.err.println("IllegalArgument");
            System.exit(-1);
        }

        System.out.println(isPrime(nbr) + " " + iteration);
        scanner.close();
    }

    public static boolean isPrime(int n) {
        iteration = 0;

        iteration++;
        if (n <= 3) {
            return true;
        }

        iteration++;
        if (n % 2 == 0) {
            return false;
        }

        iteration++;
        if (n % 3 == 0) {
            return false;
        }

        int i = 5;
        while (true) {
            iteration++;
            if (i * i > n) {
                break;
            }

            iteration++;
            if (n % i == 0) {
                return false;
            }

            iteration++;
            if (n % (i + 2) == 0) {
                return false;
            }

            i += 6;
        }

        return true;
    }
}