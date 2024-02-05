package ex00;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * Класс для анализа файловых сигнатур.
 */
public class FileSignatures {

    private static final String SIGNATURES_FILE_PATH = "src/ex00/signatures.txt";
    private static final String RESULT_FILE_PATH = "src/ex00/result.txt";
    private static final String UNDEFINED = "UNDEFINED";
    private static final String PROCESSED = "PROCESSED";
    private static final String EXIT_COMMAND = "42";

    private int maxSignatureLength;

    /**
     * Конструктор класса FileSignatures.
     */
    public FileSignatures() {
        maxSignatureLength = -1;

        List<String> inputLines = readSignaturesFile(SIGNATURES_FILE_PATH);
        Objects.requireNonNull(inputLines, "Invalid signatures file");

        Map<String, byte[]> extensionToSignature = parseSignatureLinesAndCreateMap(inputLines);
        Objects.requireNonNull(extensionToSignature, "Invalid signature");

        run(extensionToSignature);
    }

    /**
     * Запуск анализа сигнатур файлов.
     *
     * @param extensionToSignature Map, содержащая соответствие расширения и сигнатуры.
     */
    private void run(Map<String, byte[]> extensionToSignature) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                String inputLine = scanner.nextLine();
                if (inputLine.equals(EXIT_COMMAND)) {
                    System.exit(0);
                }

                File inputFile = new File(inputLine);

                if (inputFile.exists() && inputFile.isFile()) {
                    String resultExtension = detectFileTypeBySignature(inputFile, extensionToSignature);
                    saveResultToFile(resultExtension);
                } else {
                    System.out.println("Invalid file path.");
                }
            }
        }
    }

    /**
     * Определение типа файла по его сигнатуре.
     *
     * @param file                 Файл для анализа.
     * @param extensionToSignature Map, содержащая соответствие расширения и сигнатуры.
     * @return Тип файла или UNDEFINED, если тип не определен.
     */
    private String detectFileTypeBySignature(File file, Map<String, byte[]> extensionToSignature) {
        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] fileBytes = inputStream.readAllBytes();

            for (Map.Entry<String, byte[]> entry : extensionToSignature.entrySet()) {
                String currentExtension = entry.getKey();
                byte[] currentSignature = entry.getValue();

                if (fileBytes.length < currentSignature.length) continue;

                boolean isMatch = Arrays.equals(Arrays.copyOfRange(fileBytes, 0, currentSignature.length), currentSignature);

                if (isMatch) {
                    return currentExtension;
                }
            }

            if (fileBytes.length >= maxSignatureLength) {
                return UNDEFINED;
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + file.getName());
        }

        return UNDEFINED;
    }

    /**
     * Запись результата анализа в файл.
     *
     * @param extension Тип файла или UNDEFINED.
     */
    private void saveResultToFile(String extension) {
        if (extension == null) {
            System.out.println(UNDEFINED);
            return;
        }

        try (OutputStream outputStream = new FileOutputStream(RESULT_FILE_PATH, true)) {
            writeLineToFile(outputStream, extension);
            System.out.println(PROCESSED);
        } catch (IOException e) {
            System.err.println("Error writing to result file: " + e.getMessage());
        }
    }

    /**
     * Запись строки в файл.
     *
     * @param outputStream Поток вывода в файл.
     * @param line         Строка для записи.
     * @throws IOException В случае ошибки записи в файл.
     */
    private void writeLineToFile(OutputStream outputStream, String line) throws IOException {
        outputStream.write((line + "\n").getBytes());
    }

    /**
     * Чтение содержимого файла с сигнатурами.
     *
     * @param filePath Путь к файлу с сигнатурами.
     * @return Список строк из файла с сигнатурами.
     */
    private List<String> readSignaturesFile(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Error reading signatures file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Парсинг строк с сигнатурами для создания Map.
     *
     * @param lines Список строк с сигнатурами.
     * @return Map, содержащая соответствие расширения и сигнатуры.
     */
    private Map<String, byte[]> parseSignatureLinesAndCreateMap(List<String> lines) {
        Map<String, byte[]> extensionToSignature = new HashMap<>();

        for (String line : lines) {
            Map.Entry<String, byte[]> entry = parseSignatureLine(line);
            if (entry == null) return null;

            String extension = entry.getKey();
            byte[] signature = entry.getValue();

            updateMaxSignatureLength(signature);

            extensionToSignature.put(extension, signature);
        }

        return extensionToSignature;
    }

    /**
     * Парсинг одной строки с сигнатурой.
     *
     * @param line Строка с сигнатурой.
     * @return Entry, содержащая расширение и сигнатуру.
     */
    private Map.Entry<String, byte[]> parseSignatureLine(String line) {
        int commaPos = line.indexOf(',');
        if (commaPos == -1) return null;

        String extension = line.substring(0, commaPos);
        byte[] signature = parseSignatureString(line.substring(commaPos + 1));

        if (signature == null) return null;

        return new AbstractMap.SimpleEntry<>(extension, signature);
    }

    /**
     * Парсинг строки с сигнатурой для получения массива байт.
     *
     * @param signatureStr Строка с сигнатурой в HEX формате.
     * @return Массив байт с сигнатурой.
     */
    private byte[] parseSignatureString(String signatureStr) {
        if (signatureStr.length() % 3 != 0) return null;

        byte[] signature = new byte[signatureStr.length() / 3];

        for (int i = 0; i < signatureStr.length(); i += 3) {
            if (signatureStr.charAt(i) != ' ') return null;

            int byte1 = hexByte(signatureStr.charAt(i + 1));
            int byte2 = hexByte(signatureStr.charAt(i + 2));

            if (byte1 == -1 || byte2 == -1) return null;

            signature[i / 3] = (byte) ((byte1 << 4) + byte2);
        }

        return signature;
    }

    /**
     * Обновление максимальной длины сигнатуры.
     *
     * @param signature Массив байт с сигнатурой.
     */
    private void updateMaxSignatureLength(byte[] signature) {
        if (signature.length > maxSignatureLength) maxSignatureLength = signature.length;
    }

    /**
     * Преобразование символа в значение байта.
     *
     * @param hex Символ в шестнадцатеричной системе.
     * @return Значение байта.
     */
    private static int hexByte(char hex) {
        if (Character.isDigit(hex)) {
            return Character.digit(hex, 16);
        } else if (Character.toUpperCase(hex) >= 'A' && Character.toUpperCase(hex) <= 'F') {
            return Character.toUpperCase(hex) - 'A' + 10;
        } else {
            return -1;
        }
    }
}
