import java.util.Scanner;

public class Program {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long storage = 0;
        long  multiplier = 1;
        int order = 1;

        

		while (true) {
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
            for (int i = 0; i < 5; i++) {
                int current = getNumber(scanner.next());
                if (current < 1 || current > 9) {
                    scanner.close();
                    endProgram(true);
                } else if (current < minGrade) {
                    minGrade = current;
                }
            }
            
            storage = storage + (long) minGrade * multiplier;
            multiplier *= 10;
            order++;
            if (order > 18) {
                break;
            }
		}

        int week = 1;
        while (week < order) {
            int grade = (int)(storage % 10);
            System.out.print("Week ");
            System.out.print(week);
            System.out.print(" ");
            printEquals('=', grade);
            System.out.println(">");
            storage /= 10;
            week++;
        }

        scanner.close();
		return;
    }

    public static int getNumber(String input) {
        switch (input) {
            case "1":
                return 1;
            case "2":
                return 2;
            case "3":
                return 3;
            case "4":
                return 4;
            case "5":
                return 5;
            case "6":
                return 6;
            case "7":
                return 7;
            case "8":
                return 8;
            case "9":
                return 9;
            case "10":
                return 10;
            case "11":
                return 11;
            case "12":
                return 12;
            case "13":
                return 13;
            case "14":
                return 14;
            case "15":
                return 15;
            case "16":
                return 16;
            case "17":
                return 17;
            case "18":
                return 18;
            default:
                System.err.println("IllegalArgument");
                System.exit(-1);
                return -1;
        }
    }

    public static void endProgram(boolean isError) {
        if (isError) {
            System.err.println("IllegalArgument");
            System.exit(-1);
        } else {
            System.exit(0);
        }
    }

    public static void printEquals(char c,int grade) {
        for (int i = 0; i < grade; i++) {
            System.out.print(c);
        }
    }
}
