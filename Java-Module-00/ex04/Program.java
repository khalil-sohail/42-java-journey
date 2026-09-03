import java.util.Scanner;

public class Program {
    public static Scanner   scanner = new Scanner(System.in);
    public static int[][]   topValuesIndexes = new int[2][10]; // 0 index values | 1 values
    public static int[]     frequency = new int[65536];
    public static int[]     topScaledValues = new int[10];
    public static int index = 0;

    public static void main(String[] args) {
        String input = scanner.nextLine();
        char[]  lineChars = input.toCharArray();

        for (int i = 0; i < lineChars.length; i++) {
            frequency[lineChars[i]]++;
        }

        for (int i = 0; i < frequency.length; i++) {
            if (frequency[i] > 0) {
                if (frequency[i] > topValuesIndexes[1][9]) {
                    topValuesIndexes[0][9] = i;
                    topValuesIndexes[1][9] = frequency[i];
                    topValuesIndexes = sortSmall2DArray(topValuesIndexes);
                }
            }
        }

        display();
        scanner.close();
		return;
    }

    public static void display() {
        for (int i = 0; i < topValuesIndexes[0].length; i++) {
            if (topValuesIndexes[1][i] == 0) break;

            topScaledValues[i] = scaleNumber(
                topValuesIndexes[1][i],
                topValuesIndexes[1][0],
                0
            );
        }

        printHistogramValues(true, 10);
        for (int row = 10; row > 0; row--) {
            for (int i = 0; i < topValuesIndexes[0].length; i++) {
                if (topValuesIndexes[1][i] == 0) break;

                int scaled = topScaledValues[i];
                if (scaled >= row) {
                    if (i == 0 || (1 + i >= topValuesIndexes[0].length && topValuesIndexes[1][i + 1] != 0)) {
                        System.out.print("#");
                    }
                    else System.out.print(" #");
                }
            }

            printHistogramValues(false, row - 1);
            System.out.println();
        }

        for (int i = 0; i < topValuesIndexes[0].length; i++) {
            if (topValuesIndexes[1][i] == 0) break;

            System.out.print((char)topValuesIndexes[0][i]);
            if (i < topValuesIndexes[0].length - 1
                    && topValuesIndexes[1][i + 1] != 0) {
                System.out.print(" ");
            }
        }
        
        System.out.println();
    }

    public static void printHistogramValues(boolean printNewLine, int row) {
        int baseIndex = index;

        while (index < topValuesIndexes[1].length
            && topScaledValues[index] == row
            && topValuesIndexes[1][index] != 0) {
                
            if (!printNewLine && index == baseIndex) System.out.print(" ");
            System.out.print(topValuesIndexes[1][index++]);            
            if (index < topValuesIndexes[1].length
                    && topScaledValues[index] == row) System.out.print(" ");
            else if (printNewLine) System.out.println();
        }
    }

    public static int scaleNumber(int number, int max, int min) {
        if (number > 0 && max > min) {
            return (int) (10 * (number - min) / (max - min));
        }
        return 0;
    }


    public static int[][] sortSmall2DArray(int[][] numbers) {
        int temp = 0;

        for (int i = 0; i < numbers[1].length; i++) {
            for (int j = 0; j < numbers[1].length; j++) {
                if (numbers[1][i] > numbers[1][j] || (numbers[0][i] < numbers[0][j] && numbers[1][i] == numbers[1][j])) {
                    temp = numbers[0][i];
                    numbers[0][i] = numbers[0][j];
                    numbers[0][j] = temp;
                    
                    temp = numbers[1][i];
                    numbers[1][i] = numbers[1][j];
                    numbers[1][j] = temp;
                }
            }
        }
        
        return numbers;
    }

    public static void printEquals(char c,int grade) {
        for (int i = 0; i < grade; i++) {
            System.out.print(c);
        }
    }

}
