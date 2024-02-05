package ex01;

import java.io.*;
import java.util.List;
import java.util.Set;

public class FileService {
    /**
     * Читает содержимое файла и добавляет слова в указанный список и словарь.
     *
     * @param filePath       Путь к файлу.
     * @param wordDictionary Словарь для добавления слов из файла.
     * @param content        Список слов для добавления содержимого файла.
     */
    public static void readContent(String filePath, Set<String> wordDictionary, List<String> content) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String word : line.split(" ")) {
                    content.add(word);
                    wordDictionary.add(word);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Сохраняет словарь в файл.
     *
     * @param filePath       Путь к файлу, в который нужно сохранить словарь.
     * @param wordDictionary Словарь для сохранения.
     */
    public static void saveDictionaryToFile(String filePath, Set<String> wordDictionary) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String word : wordDictionary) {
                writer.write(word);
                writer.write(" ");
            }
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error writing to dictionary file: " + e.getMessage());
        }
    }
}
