import java.util.Scanner;

public class Program {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long storage = 0;
        int order = 1;
        int multiplier = 1;

		while (true) {
            System.out.print("-> ");
            String word = scanner.next();
            if (word.equals("42")) {
                break;
            }

            int nbr = getNumber(scanner.next());
            if (!word.equals("Week") || nbr != order) {
                scanner.close();
                endProgram(true);
            }

            
            int minGrade = 9;
            System.out.print("-> ");
            for (int i = 0; i < 5; i++) {
                int current = getNumber(scanner.next());
                if (current < 1 || current > 9) {
                    scanner.close();
                    endProgram(true);
                } else if (current < minGrade) {
                    minGrade = current;
                }
            }
            
            storage = storage + (long)(minGrade * multiplier);
            multiplier *= 10;
            order++;
            if (order > 18) {
                break;
            }
		}

        int week = 1;
        while (week < order) {
            int grade = (int)(storage % 10);
            System.out.println("Week " + week + (week > 9 ? " " : " ") + "=".repeat(grade) + ">");
            storage /= 10;
            week++;
        }

        scanner.close();
		return;
    }

    public static int getNumber(String input) {
        int current = 0;

        if (input.equals("1")) current = 1;
        else if (input.equals("2")) current = 2;
        else if (input.equals("3")) current = 3;
        else if (input.equals("4")) current = 4;
        else if (input.equals("5")) current = 5;
        else if (input.equals("6")) current = 6;
        else if (input.equals("7")) current = 7;
        else if (input.equals("8")) current = 8;
        else if (input.equals("9")) current = 9;
        else return -1;
        
        return current;
    }

    public static void endProgram(boolean isError) {
        if (isError) {
            System.err.println("IllegalArgument");
            System.exit(-1);
        } else {
            System.exit(0);
        }
    }
}
