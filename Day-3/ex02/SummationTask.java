package src.ex02;

public class SummationTask implements Runnable {
    private final ResultSummarizer resultSummarizer;
    private int start = 0;
    private int end = 0;
    private final int[] arrays;

    public SummationTask(ResultSummarizer resultSummarizer, int[] arrays, int start, int end, int threadsCount) {
        this.resultSummarizer = resultSummarizer;
        this.arrays = arrays;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        resultSummarizer.sum(arrays, start, end);
    }
}
