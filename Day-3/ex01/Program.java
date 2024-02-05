package src.ex01;

public class Program {
    public static final Object lock = new Object();
    public static void main(String[] args) {

        if(args.length != 1 || !args[0].startsWith("--count=")){
            System.err.println("Usage: java Main --count=<number>");
            System.exit(1);
        }
        int count = Integer.parseInt(args[0].substring(8));
        System.out.println("Count: " + count);

        Thread eggThread = new EggThread(count);
        Thread henThread = new HenThread(count);

        eggThread.start();
        henThread.start();

    }
}
