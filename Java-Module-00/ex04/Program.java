import java.util.Scanner;

public class Program {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        
        char[] lineChars = input.toCharArray();
        int[] frequency = new int[65536];
        int[][] topValuesIndexes = new int[2][10]; // 0 index values | 1 values


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

        System.out.println();
        for (int i = 0; i < topValuesIndexes[0].length; i++) {
            if (topValuesIndexes[1][i] > 0) {
                System.out.print((char)topValuesIndexes[0][i] + " | ");
                
                int rep = scaleNumber(topValuesIndexes[1][i], topValuesIndexes[1][0], 0);
                for (int j = 0; j < rep; j++) {
                    System.out.print("# ");
                }

                printEquals(' ', 20 - rep*2);
                System.out.print(topValuesIndexes[1][i]);
                System.out.println();
            }
        }

        scanner.close();
		return;
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
