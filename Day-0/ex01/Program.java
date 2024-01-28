package src.ex01;

import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int divisor = 2;

        if (scanner.hasNextInt()) {
            int numberToCheck = scanner.nextInt();

            if (numberToCheck <= 1) {
                System.out.println("IllegalArgument");
                System.exit(-1);
            }

            for (; divisor * divisor <= numberToCheck; divisor++) {
                if (numberToCheck % divisor == 0) {
                    System.out.println("false " + --divisor);
                    System.exit(0);
                }
            }

            System.out.println("true " + --divisor);
        }
    }
}
