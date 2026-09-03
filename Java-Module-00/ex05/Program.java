import java.util.Scanner;

public class Program {
    public static String[] studentList = new String[10];
    public static int[][] classList = new int[10][2];
    public static int[][] attendanceList = new int[505][4];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        parseStudentList(scanner);
        if (studentList[0] == null) {
            System.out.println("No students in the list. Exiting program.");
            exitProgram(scanner);
        }

        parseTimeTable(scanner);
        if (classList[0][0] == 0) {
            System.out.println("No classes in the timetable. Exiting program.");
            exitProgram(scanner);
        }

        parseAttendanceRecord(scanner);
        if (attendanceList[0][0] == 0) {
            System.out.println("No attendance records. Exiting program.");
            exitProgram(scanner);
        }

        displayAttendance();
        scanner.close();
        return;
    }

    public static void parseStudentList(Scanner scanner) {
        int i = 0;
        while (true) {
            String input = scanner.nextLine();

            if (input.equals(".")) {
                break;
            } else if (i >= 10) {
                System.out.println("Total classes per week cannot exceed 10.");
                exitProgram(scanner);
            }

            if (NotValidName(input)) {
                exitProgram(scanner);
            }
            
            studentList[i] = input;
            i++;
        }

        return;
    }
    
    public static void parseTimeTable(Scanner scanner) {
        int i = 0;
        while (true) {
            String input = scanner.next();
            if (input.equals(".")) {
                break;
            } else if (i >= 10) {
                System.out.println("Maximum number of students in the timetable is also 10.");
                exitProgram(scanner);
            }

            int time = getNumber(input);
            if (time < 1 || time > 6) {
                System.out.println("Error: Time must be between `1` and `6`.");
                exitProgram(scanner);
            }

            String day = scanner.next();
            int dayNumber = getDayNumber(day);
            if (dayNumber == -1) {
                System.out.println("Error: Day must be one of `MO`, `TU`, `WE`, `TH`, `FR`, `SA`, `SU`.");
                exitProgram(scanner);
            }

            classList[i][0] = time;
            classList[i][1] = dayNumber;
            i++;
        }

        return;
    }
    
    public static void parseAttendanceRecord(Scanner scanner) {
        for (int i = 0; i < attendanceList.length; i++) {
            String name = scanner.next();
            if (name.equals(".")) {
                break;
            }

            int studentIndex = getStudentIndex(name);
            if (NotValidName(name) || studentIndex == -1) {
                exitProgram(scanner);
            }

            String input = scanner.next();
            int time = getNumber(input);
            if (time < 1 || time > 6) {
                System.out.println("Error: Time must be between `1` and `6`.");
                exitProgram(scanner);
            }

            String day = scanner.next();
            int dayNumber = getNumber(day);
            if (dayNumber < 1 || dayNumber > 30) {
                System.out.println("Error: Day must be between `1` and `30`.");
                exitProgram(scanner);
            }

            String status = scanner.next();
            if (!status.equals("NOT_HERE") && !status.equals("HERE")) {
                System.out.println("Error: Status must be either `NOT_HERE` or `HERE`.");
                exitProgram(scanner);
            }

            attendanceList[i][0] = studentIndex;
            attendanceList[i][1] = time;
            attendanceList[i][2] = dayNumber;
            attendanceList[i][3] = status.equals("HERE") ? 1 : -1;
        }
        return;
    }

    public static void displayAttendance() {
        int dayNbr = 2; // TU

        System.out.print(" ");
        for (int date = 1; date <= 30; date++) {

            for (int time = 1; time <= 6; time++) {
                for (int i = 0; i < classList.length; i++) {
                    if (classList[i][0] == 0) {
                        break;
                    }

                    if (classList[i][0] == time &&
                        classList[i][1] == dayNbr) {

                        String dayName = getDayName(dayNbr);
                        System.out.print(
                            time + ":00 " +
                            dayName + " " +
                            date
                        );
                        System.out.print("|");
                    }
                }
            }

            dayNbr++;
            if (dayNbr > 7) {
                dayNbr = 1;
            }
        }

        System.out.println();

        for (int student = 0; student < studentList.length; student++) {
            if (studentList[student] == null) {
                break;
            }

            System.out.print(studentList[student]);
            for (int i = studentList[student].length(); i < 10; i++) {
                System.out.print(" ");
            }
            System.out.print("|");

            dayNbr = 2; // TU
            for (int date = 1; date <= 30; date++) {
                for (int time = 1; time <= 6; time++) {
                    boolean classExists = false;
                    for (int i = 0; i < classList.length; i++) {
                        if (classList[i][0] == 0) {
                            break;
                        }
                        if (classList[i][0] == time &&
                            classList[i][1] == dayNbr) {

                            classExists = true;
                            break;
                        }
                    }
                    if (!classExists) {
                        continue;
                    }

                    int status = 0;
                    for (int i = 0; i < attendanceList.length; i++) {
                        if (attendanceList[i][3] == 0) {
                            continue;
                        }
                        if (attendanceList[i][0] == student &&
                            attendanceList[i][1] == time &&
                            attendanceList[i][2] == date) {

                            status = attendanceList[i][3];
                            break;
                        }
                    }

                    if (status == 1) {
                        System.out.print("       1|");
                    } else if (status == -1) {
                        System.out.print("      -1|");
                    } else {
                        System.out.print("        |");
                    }
                }

                dayNbr++;
                if (dayNbr > 7) {
                    dayNbr = 1;
                }
            }

            System.out.println();
        }
    }

    public static void exitProgram(Scanner scanner) {
        scanner.close();
        System.exit(-1);
    }

    public static int getStudentIndex(String name) {
        for (int i = 0; i < studentList.length; i++) {
            if (studentList[i] == null) {
                break;
            }
            if (studentList[i].equals(name)) {
                return i;
            }
        }
        System.out.println("Error: Student not found in the list.");
        return -1;
    }

    public static boolean NotValidName(String name) {
        if (name.length() == 0 || name.length() > 10) {
            System.out.println("Error: Name must be between `1` and `10` characters.");
            return true;
        }

        char[] chars = name.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ') {
                System.out.println("Error: Name cannot contain spaces.");
                return true;
            }
        }

        return false;
    }

    public static int getNumber(String input) {
        char[] chars = input.toCharArray();
        int number = 0;

        for (int i = 0; i < chars.length; i++) {
            int digit = chars[i] - '0';
            number = number * 10 + digit;
        }

        return number;
    }
    
    public static int getDayNumber(String day) {
        switch (day) {
            case "MO":
                return 1;
            case "TU":
                return 2;
            case "WE":
                return 3;
            case "TH":
                return 4;
            case "FR":
                return 5;
            case "SA":
                return 6;
            case "SU":
                return 7;
            default:
                return -1;
        }
    }

    public static String getDayName(int dayNumber) {
        switch (dayNumber) {
            case 1:
                return "MO";
            case 2:
                return "TU";
            case 3:
                return "WE";
            case 4:
                return "TH";
            case 5:
                return "FR";
            case 6:
                return "SA";
            case 7:
                return "SU";
            default:
                return "";
        }
    }
}
