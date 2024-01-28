package src.ex00;

public class Program {
    public static void main(String[] args) {
        int num = 479598;
        int res = 0;
        while (num > 0) {
            int dig = num % 10;
            res += dig;
            num = num / 10;
        }
        System.out.println(res);
    }

}