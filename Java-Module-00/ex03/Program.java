import java.util.Scanner;

public class Program {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long storage = 0;
        int order = 1;

		while (true) {
            System.out.print("-> ");
            String word = scanner.next();
            if (word.equals("42")) {
                break;
            }

            int nbr = scanner.nextInt();
            if (!word.equals("Week") || nbr != order) {
                System.err.print("IllegalArgument");
                scanner.close();
                System.exit(-1);
            }
            
            int minGrade = Integer.MAX_VALUE;
            System.out.print("-> ");
            for (int i = 0; i < 5; i++) {
                int current = scanner.nextInt();
                if (current < minGrade) {
                    minGrade = current;
                }
            }
            
            storage = storage + (long)(minGrade * Math.pow(10, order - 1));
            order++;
		}

        System.out.println(storage);

        int week = 1;
        while (week < order) {
            int grade = (int)(storage % 10);
            System.out.println("Week " + week + " " + "=".repeat(grade) + ">");
            storage = storage / 10;
            week++;
        }
        scanner.close();        
		return;
    }
}