package src.ex04;

import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        int[] counts = countCharacters(input);

        int[] topCounts = new int[10];
        char[] topChars = new char[10];

        updateTopCountsAndChars(counts, topCounts, topChars);

        printHistogram(topCounts);

        printTopChars(topChars);
    }

    private static int[] countCharacters(String input) {
        int[] counts = new int[65536];
        for (char c : input.toCharArray()) {
            counts[c]++;
        }
        return counts;
    }

    private static void updateTopCountsAndChars(int[] counts, int[] topCounts, char[] topChars) {
        for (int i = 0; i < counts.length; i++) {
            int count = counts[i];

            for (int j = 0; j < topCounts.length; j++) {
                if (count > topCounts[j]) {
                    updateTopCountsArray(topCounts, j, count);
                    updateTopCharsArray(topChars, j, i);
                    break;
                } else if (count == topCounts[j] && i < topChars[j]) {
                    updateTopCharsArray(topChars, j, i);
                    break;
                }
            }
        }
    }

    private static void updateTopCountsArray(int[] topCounts, int index, int count) {
        for (int k = topCounts.length - 1; k > index; k--) {
            topCounts[k] = topCounts[k - 1];
        }
        topCounts[index] = count;
    }

    private static void updateTopCharsArray(char[] topChars, int index, int i) {
        for (int k = topChars.length - 1; k > index; k--) {
            topChars[k] = topChars[k - 1];
        }
        topChars[index] = (char) i;
    }

    private static void printHistogram(int[] topCounts) {
        int maxCount = topCounts[0];
        System.out.println();
        System.out.println();
        for (int i = 0; i < 10; i++) {
            if (topCounts[i] == maxCount)
                System.out.print(topCounts[i] + "\t");
        }
        System.out.println();
        for (int i = 10; i > 0; i--) {
            for (int j = 0; j < 10; j++) {
                if (topCounts[j] * 10 / maxCount >= i)
                    System.out.print("#\t");
                if (topCounts[j] * 10 / maxCount == i - 1) {
                    if (topCounts[j] != 0) {
                        System.out.print(topCounts[j] + "\t");
                    }
                }
            }
            System.out.println();
        }
    }

    private static void printTopChars(char[] topChars) {
        for (int i = 0; i < 10; i++) {
            System.out.print(topChars[i] + "\t");
        }
    }
}