package src.ex02;

import java.util.Arrays;

public class Program {
    private static int arraySize;
    private static int threadsCount;

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2
                || !args[0].startsWith("--arraySize=")
                || !args[1].startsWith("--threadsCount=")) {
            System.err.println("Usage: java Program --arraySize=<size> --threadsCount=<count>");
            System.exit(-1);
        }
        arraySize = Integer.parseInt(args[0].substring(12));
        threadsCount = Integer.parseInt(args[1].substring(15));

        if (arraySize <= 0 || threadsCount <= 0 || arraySize > 2_000_000) {
            System.err.println("Invalid arguments: arraySize should be between 1 and 2,000,000, threadsCount should be greater than 0");
            System.exit(-1);
        }


        int sum = calculateAndPrintSum();

        System.out.println("Sum: " + sum);
    }

    private static int calculateAndPrintSum() throws InterruptedException {
        int[] mainArray = generateRandomArray(arraySize);
        System.out.println("sum standard: " + calcSum(mainArray));
        int chunkSize = calculateChunkSize(arraySize, threadsCount);
        Thread[] threads = new Thread[threadsCount];
        ResultSummarizer resultSummarizer = new ResultSummarizer();

        int start = 0;
        for (int i = 0; i < threadsCount; i++) {
            int end = Math.min(start + chunkSize - 1, arraySize - 1);
            threads[i] = new Thread(new SummationTask(resultSummarizer, Arrays.copyOfRange(mainArray, start, end + 1),
                    start, end, threadsCount));
            start += chunkSize;
        }

        startThreadsAndWait(threads);

        return resultSummarizer.getSumOfThreads();
    }

    public static int calcSum(int[] array) {
        int sum = 0;
        for (int element : array) {
            sum += element;
        }
        return sum;
    }

    private static int[] generateRandomArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = (int) (Math.random() * 1000);
//            array[i] = 1;  // массив из тз
        }
        return array;
    }

    private static int calculateChunkSize(int arraySize, int threadsCount) {
        int chunkSize = arraySize / threadsCount;
        int remainder = arraySize % threadsCount;
        if (remainder > 0) {
            chunkSize++;
        }
        return chunkSize;
    }

    private static void startThreadsAndWait(Thread[] threads) throws InterruptedException {
        for (Thread thread : threads) {
            thread.start();
            thread.join();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

    public static int getThreadsCount() {
        return threadsCount;
    }

}
