
public class Program {
    public static void main(String[] args) {
        int nbr = 479598;
        int res = sumDigits(nbr);
        System.out.println(res);
    }

    public static int sumDigits(int n) {
        return n / 100000
            + (n / 10000) % 10
            + (n / 1000) % 10
            + (n / 100) % 10
            + (n / 10) % 10
            + n % 10;
    }
}
