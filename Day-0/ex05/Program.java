package src.ex05;

import java.util.Scanner;

public class Program {
    private static final Scanner scanner = new Scanner(System.in);
    private final String[] students;
    private final String[][] schedule;
    private final String[][][][] attendance;

    Program() {
        this.students = new String[10];
        this.schedule = new String[7][10];
        this.attendance = new String[10][30][10][1];
    }

    public static void main(String[] args) {
        Program shed = new Program();
        shed.createStudentList();
        shed.populateSchedule();
        shed.recordAttendance();
        shed.displayTimetable();
    }

    private void createStudentList() {
        String name;
        int i = 0;
        while (scanner.hasNext() && !(name = scanner.next()).equals(".") && i < 10) {
            if (name.length() < 10) {
                students[i] = name;
            } else {
                System.err.println("Illegal Argument");
                System.exit(-1);
            }
            i++;
        }
    }

    private void populateSchedule() {
        String time;
        String date;
        int j = 0;
        while (scanner.hasNext() && !(time = scanner.next()).equals(".")) {
            isScannered();
            date = scanner.next();
            fillSchedule(schedule, date, time);
            j++;
        }
    }

    private void recordAttendance() {
        String name;
        String time;
        String date;
        String status;
        int i, week;
        while (scanner.hasNext() && !(name = scanner.next()).equals(".")) {
            week = 0;
            isScannered();
            time = scanner.next();
            isScannered();
            date = scanner.next();
            isScannered();
            status = scanner.next();
            week = findClassIndex(Integer.parseInt(date), schedule, time);
            for (i = 0; i < students.length && !students[i].equals(name); i++) ;
            attendance[i][Integer.parseInt(date) - 1][week][0] = status;
        }
    }

    private void displayTimetable() {
        int i;
        for (i = 0; i <= 30; i++) {
            display(i, schedule);
        }
        System.out.println();

        for (i = 0; i < students.length && students[i] != null; i++) {
            System.out.printf("%10s", students[i]);
            display(attendance[i], schedule, students[i].length());
        }
    }

    private int findClassIndex(int date, String[][] classes, String time) {
        int dayOfWeek = date % 7;
        for (int j = 0; j < classes[dayOfWeek].length; j++) {
            if (classes[dayOfWeek][j] != null && classes[dayOfWeek][j].equals(time)) {
                return j;
            }
        }

        return -1;
    }

    private void fillSchedule(String[][] schedule, String tmp, String time) {
        int dayIndex = getDayIndex(tmp);
        schedule[dayIndex][0] = time;
        schedule[dayIndex][1] = tmp;
    }

    private int getDayIndex(String day) {
        switch (day) {
            case "MO":
                return 0;
            case "TU":
                return 1;
            case "WE":
                return 2;
            case "TH":
                return 3;
            case "FR":
                return 4;
            case "SA":
                return 5;
            case "SU":
                return 6;
            default:
                return -1;
        }
    }


    private static void display(String[][][] attendance, String[][] schedule, int len) {
        for (int i = 0; i < 30; i++) {
            int day = (i + 1) % 7;
            int j = 0;

            while (j < 1 && schedule[day][j] != null) {
                printAttendanceStatus(attendance[i][j][0]);
                j++;
            }
        }
        System.out.println();
    }

    private static void printAttendanceStatus(String status) {
        if (status != null) {
            switch (status) {
                case "HERE":
                    System.out.printf("        %2d|", 1);
                    break;
                case "NOT_HERE":
                    System.out.printf("        %2d|", -1);
                    break;
                default:
                    System.out.print("          |");
            }
        } else {
            System.out.print("          |");
        }
    }


    private static void display(int i, String[][] schedule) {
        if (i == 0) {
            System.out.print("          ");
        }

        int day = ++i % 7;
        int j = 0;

        while (j < 1 && schedule[day][j] != null) {
            System.out.print(schedule[day][j] + ":00 ");
            printDayAbbreviation(day);
            System.out.printf("%2d|", i);
            j++;
        }
    }

    private static void printDayAbbreviation(int day) {
        String[] dayAbbreviations = {"MO", "TU", "WE", "TH", "FR", "SA", "SU"};
        if (day >= 0 && day < dayAbbreviations.length) {
            System.out.printf("%s ", dayAbbreviations[day]);
        }
    }


    private static void isScannered() {
        if (!scanner.hasNext()) {
            System.err.println("Illegal Argument");
            System.exit(-1);
        }
    }
}
