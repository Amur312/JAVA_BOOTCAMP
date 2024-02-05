package src.ex03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class FileDownloader {
    private final String fileName;
    private final List<String> taskQueue;

    public FileDownloader(String fileName) {
        this.fileName = fileName;
        this.taskQueue = new LinkedList<>();
    }

    public List<String> readUrlsFromFile() throws IOException {
        return Files.readAllLines(Path.of(fileName));
    }

    public void distributesToThreads(List<String> urls, int threadCount) {
        taskQueue.addAll(urls);
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new DownloadThread(this, i + 1));
            threads[i].start();
        }
        try {
            for (Thread thread : threads) {

                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public synchronized String getNextTask() {
        if (!taskQueue.isEmpty()) {
            return taskQueue.remove(0);
        }
        return null;
    }
}
