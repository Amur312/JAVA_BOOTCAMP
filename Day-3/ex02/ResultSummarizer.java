package src.ex02;

public class ResultSummarizer {
    private int result = 0;
    private int count = 1;
    private final Object lock = new Object();

    public void sum(int[] arrays, int start, int end) {
        int sum = 0;
        for (int i : arrays) {
            sum += i;
        }
        synchronized (lock) {
            System.out.println("Thread " + count + ": from " +
                    start + " to " + end + " sum is " + sum);
            result += sum;
            count++;
            if (count > Program.getThreadsCount()) {
                lock.notifyAll();
            }
        }
    }

    public int getSumOfThreads() throws InterruptedException {
        synchronized (lock) {
            int threadsCount = Program.getThreadsCount();
            while (count <= threadsCount) {
                lock.wait();
            }
            return result;
        }
    }
}
