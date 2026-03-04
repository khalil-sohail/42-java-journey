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
        iteration = 1;
        if (n <= 1) {
            return false;
        } if (n <= 3) {
            return true;
        } if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }

        int i = 5;
        while (i * i <= n) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
            i += 6;
            iteration += 1;
        }
        return true;
    }
}