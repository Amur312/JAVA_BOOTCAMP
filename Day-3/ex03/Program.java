package src.ex03;

import java.io.IOException;
import java.util.List;

public class Program {
    public static void main(String[] args) {
        if (args.length != 1 || !args[0].startsWith("--threadsCount=")) {
            System.err.println("Usage: java Program --threadsCount=<count>");
            System.exit(-1);
        }
        int count = Integer.parseInt(args[0].substring(15));

        String fileName = "src/ex03/files_urls.txt";
        FileDownloader fileDownloader = new FileDownloader(fileName);
        try {
            List<String> urls = fileDownloader.readUrlsFromFile();
            fileDownloader.distributesToThreads(urls, count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
