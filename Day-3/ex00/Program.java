package src.ex00;

public class Program {
    public static void main(String[] args) {
        if (args.length != 1 || !args[0].startsWith("--count=")) {
            System.err.println("Usage: java Program --count=<number>");
            System.exit(1);
        }
        int count = Integer.parseInt(args[0].substring("--count=".length()));
        Thread eggThread = new Thread(new EggHenHumanRunnable("Egg", count));
        Thread henThread = new Thread(new EggHenHumanRunnable("Hen", count));

        eggThread.start();
        henThread.start();

        for (int i = 0; i < count; i++) {
            System.out.println("Human");
        }
    }
}
