package src.ex00;

public class EggHenHumanRunnable implements Runnable{
    private final String message;
    private final int count;

    public EggHenHumanRunnable(String message, int count) {
        this.message = message;
        this.count = count;
    }
    @Override
    public void run() {
        for (int i = 0; i < count; i++) {
            System.out.println(message);
            Thread.yield();
        }
    }
}
