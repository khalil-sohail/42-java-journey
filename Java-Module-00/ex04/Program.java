import java.util.Scanner;

public class Program {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("-> ");
        String input = scanner.nextLine();
        int[] numbers = new int[26];
        char[] lineChars = input.toCharArray();
        char[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        for (int i = 0; i < lineChars.length; i++) {
            if (lineChars[i] >= 'a' && lineChars[i] <= 'z') {
                numbers[lineChars[i] - 'a']++;
            } else if (lineChars[i] >= 'A' && lineChars[i] <= 'Z') {
                numbers[lineChars[i] - 'A']++;
            }
        }

        // System.out.println("numbers = " + java.util.Arrays.toString(numbers));
        // for (int i = 0; i < numbers.length; i++) {
        //     if (numbers[i] > 0) {
        //         char letter = (char) (i + 'A');
        //         System.out.println(letter + " : " + numbers[i]);
        //     }
        // }

        // To Do: Sort the letters based on their frequency in descending order - If two letters have the same frequency, sort them in alphabetical order.
        // To Do: Print the letters and their frequency in a Histogram-like format.


        scanner.close();
		return;
    }

}
