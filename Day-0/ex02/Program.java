package src.ex02;

import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int countRes = 0;
        int num;
        while ((num = scanner.nextInt()) != 42) {
            int sum = sumNum(num);
            if (isPrime(sum)) {
                countRes++;
            }
        }

        System.out.println("Count of coffee-request – " + countRes);
    }

    public static int sumNum(int num) {
        int sum = 0;
        while (num > 0) {
            int dig = num % 10;
            sum += dig;
            num = num / 10;
        }
        return sum;
    }

    public static boolean isPrime(int sum) {
        boolean check = true;
        if (sum <= 1) {
            return false;
        }
        for (int i = 2; i * i <= sum; i++) {
            if (sum % i == 0) {
                check = false;
                break;
            }
        }
        return check;
    }
}
