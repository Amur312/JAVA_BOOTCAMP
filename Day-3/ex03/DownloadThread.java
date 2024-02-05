package src.ex03;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

public class DownloadThread implements Runnable {
    private final FileDownloader fileDownloader;
    private final int threadNumber;
    private static final AtomicInteger fileCounter = new AtomicInteger(1);

    public DownloadThread(FileDownloader fileDownloader, int threadNumber) {
        this.fileDownloader = fileDownloader;
        this.threadNumber = threadNumber;
    }

    @Override
    public void run() {
        String task;
        while ((task = fileDownloader.getNextTask()) != null) {
            int currentFileNumber = fileCounter.getAndIncrement();
            System.out.println("Thread-" + threadNumber + " start download file number " + currentFileNumber);
            downloadFile(task, currentFileNumber);
            System.out.println("Thread-" + threadNumber + " finish download file number " + currentFileNumber);
        }
    }

    private void downloadFile(String url, int fileNumber) {
        try {
            URL fileUrl = new URL(url);
            String fileName = extractFileName(url, fileNumber);
            if (!checkFileExists(fileName)) {
                Files.copy(fileUrl.openStream(), Path.of("/home/amur/test/", fileName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String extractFileName(String url, int fileNumber) {
        int dotIndex = url.lastIndexOf('.');
        String extension = url.substring(dotIndex + 1);
        return "file" + fileNumber + "." + extension;
    }

    private boolean checkFileExists(String fileName) {
        return Files.exists(Path.of("/home/amur/test/", fileName));
    }
}
