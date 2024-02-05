package ex01;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileProcessor {
    private String filePath1;
    private String filePath2;
    private Set<String> wordDictionary;
    private List<String> contentList1;
    private List<String> contentList2;
    private int[] wordFrequencyVector1;
    private int[] wordFrequencyVector2;

    /**
     * Конструктор FileProcessor с указанием путей к двум файлам.
     *
     * @param filePath1 Путь к первому файлу.
     * @param filePath2 Путь ко второму файлу.
     */
    public FileProcessor(String filePath1, String filePath2) {
        this.filePath1 = filePath1;
        this.filePath2 = filePath2;
        wordDictionary = new HashSet<>();
        contentList1 = new ArrayList<>();
        contentList2 = new ArrayList<>();
    }

    /**
     * Запускает обработку файлов и вычисление схожести.
     */
    public void run() {
        FileService.readContent(filePath1, wordDictionary, contentList1);
        FileService.readContent(filePath2, wordDictionary, contentList2);
        FileService.saveDictionaryToFile("src/ex01/dictionary.txt", wordDictionary);

        wordFrequencyVector1 = analyzeWordFrequency(contentList1);
        wordFrequencyVector2 = analyzeWordFrequency(contentList2);

        System.out.print("Similarity = ");
        System.out.printf("%.2f", Math.floor(calculateSimilarity() * 100) / 100);
    }

    /**
     * Анализирует частоту встречаемости слов в списке относительно словаря.
     *
     * @param content Список слов для анализа частоты встречаемости.
     * @return Массив частот встречаемости слов.
     */
    private int[] analyzeWordFrequency(List<String> content) {
        int[] frequencyVector = new int[wordDictionary.size()];

        int i = 0;
        for (String dictionaryWord : wordDictionary) {
            for (String contentWord : content) {
                if (dictionaryWord.equals(contentWord)) {
                    frequencyVector[i]++;
                }
            }
            i++;
        }

        return frequencyVector;
    }

    /**
     * Вычисляет схожесть текстов по формуле косинусной меры.
     *
     * @return Значение схожести текстов.
     */
    private double calculateSimilarity() {
        double epsilon = 1e-6;
        double numerator = 0;
        double denominator = 0;

        for (int i = 0; i < wordFrequencyVector1.length; i++) {
            numerator += (double) wordFrequencyVector1[i] * wordFrequencyVector2[i];
        }

        double tmp1 = 0, tmp2 = 0;
        for (int frequency : wordFrequencyVector1) {
            tmp1 += Math.pow(frequency, 2);
        }
        for (int frequency : wordFrequencyVector2) {
            tmp2 += Math.pow(frequency, 2);
        }

        denominator = Math.sqrt(tmp1) * Math.sqrt(tmp2);

        if (Math.abs(denominator) < epsilon || Math.abs(numerator) < epsilon) {
            return 0;
        } else {
            return numerator / denominator;
        }
    }
}
