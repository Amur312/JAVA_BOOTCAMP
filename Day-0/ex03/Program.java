package src.ex03;

import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] weekNumbers = new int[18];
        int[] minGrades = new int[18];
        int countWeek = 0;

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals("42")) {
                break;
            }
            if (input.startsWith("Week") && countWeek < 18) {
                int numWeek = extractWeekNum(input);
                if (numWeek < 1 || numWeek > 52) {
                    System.out.println("Incorrect week numbering");
                    System.exit(1);
                    break;
                }
                countWeek++;
                processWeekData(scanner, weekNumbers, minGrades, numWeek);
            }
        }
        printStatistics(weekNumbers, minGrades, countWeek);
    }

    public static int extractWeekNum(String input) {
        String[] parts = input.split(" ");
        return Integer.parseInt(parts[1]);
    }

    private static void processWeekData(Scanner scanner, int[] weekNumbers, int[] minGrades, int numWeek) {
        int min = findMinGrade(scanner);
        for (int i = 0; i < weekNumbers.length; i++) {
            if (weekNumbers[i] == numWeek) {
                System.out.println("IllegalArgument");
                System.exit(1);
            }
            if (weekNumbers[i] == 0) {
                weekNumbers[i] = numWeek;
                minGrades[i] = min;
                break;
            }
        }
    }

    private static int findMinGrade(Scanner scanner) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < 5; i++) {
            if (scanner.hasNextInt()) {
                int num = scanner.nextInt();
                if (num < 1 || num > 9) {
                    System.out.println("IllegalArgument");
                    System.exit(1);
                }
                if (num < min) {
                    min = num;
                }
            } else {
                System.out.println("IllegalArgument");
                System.exit(1);
            }
        }
        scanner.nextLine();
        return min;
    }

    private static void printStatistics(int[] weekNumbers, int[] minGrades, int countWeek) {
        for (int i = 0; i < countWeek; i++) {
            System.out.print("Week " + weekNumbers[i] + " ");
            printMinGrade(minGrades[i]);
        }
    }

    private static void printMinGrade(int minGrade) {
        for (int i = 0; i < minGrade; i++) {
            System.out.print("=");
        }
        System.out.println(">");
    }
}
