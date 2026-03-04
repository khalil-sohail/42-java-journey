import java.util.Scanner;

public class Program {
    public static int count = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
		System.out.print("-> ");
		int nbr = scanner.nextInt();
		while (nbr != 42) {
			if (isPrime(sumDigitsRecursive(nbr))) {
				count++;
			}
			System.out.print("-> ");
			nbr = scanner.nextInt();
		}
		System.out.println("Count of coffee-request - " + count);
		scanner.close();
		return;
    }

    public static int sumDigitsRecursive(int number) {
        if (number == 0) {
            return 0;
        }

        return Math.abs(number % 10) + sumDigitsRecursive(number / 10);
    }

    public static boolean isPrime(int n) {
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
        }
        return true;
    }
}