
public class Program {
    public static void main(String[] args) {
        int nbr = 479598;
        int res = sumDigits(nbr);
        System.out.println(res);
    }

    public static int sumDigits(int n) {
        if (n == 0)
            return 0; // logic must be changed, no condition is allowed for this exercise.

        return (n % 10) + sumDigits(n / 10);
    }
}
