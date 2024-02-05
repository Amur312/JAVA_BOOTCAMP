package ex01;

public class Program {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java Program <file1> <file2>");
            System.exit(1);
        }

        try {
            new FileProcessor(args[0], args[1]).run();
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
