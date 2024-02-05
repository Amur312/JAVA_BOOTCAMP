package ex02;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;


public class FileService {
    private static final String[] FILE_SIZE_FORMATS = {"B", "KB", "MB", "GB"};

    /**
     * Перемещает файл или директорию в указанное место.
     *
     * @param sourceFile      Исходный файл или директория.
     * @param destinationFile Целевой файл или директория.
     * @throws IllegalArgumentException Если исходный или целевой файл равен null.
     * @throws IllegalArgumentException Если исходный файл не существует.
     * @throws IllegalArgumentException Если целевой файл уже существует.
     * @throws RuntimeException         Если произошла ошибка при перемещении файла.
     */
    public static void move(File sourceFile, File destinationFile) {
        if (sourceFile == null || destinationFile == null) {
            throw new IllegalArgumentException("Source and destination files must not be null.");
        }

        if (!sourceFile.exists()) {
            System.out.println(sourceFile.getAbsolutePath());
            throw new IllegalArgumentException(sourceFile.getName() + " does not exist");
        }

        if (!destinationFile.exists()) {
            sourceFile.renameTo(destinationFile);
        } else if (destinationFile.isFile()) {
            throw new IllegalArgumentException("File " + destinationFile.getName() + " already exists");
        } else if (destinationFile.isDirectory()) {
            boolean success = sourceFile.renameTo(new File(destinationFile.getAbsolutePath(), sourceFile.getName()));
            if (!success) {
                throw new RuntimeException("Error moving file");
            }
        }
    }

    /**
     * Выводит содержимое текущей рабочей директории.
     *
     * @param currentWorkingDirectory Текущая рабочая директория.
     * @throws IllegalArgumentException Если текущая рабочая директория равна null.
     */
    public static void displayContent(File currentWorkingDirectory) {
        if (currentWorkingDirectory == null) {
            throw new IllegalArgumentException("Current working directory must not be null.");
        }

        for (File file : currentWorkingDirectory.listFiles()) {
            System.out.println(file.getName() + " " + formatFileSize(getFileSize(file)));
        }
    }

    /**
     * Изменяет текущую рабочую директорию.
     *
     * @param currentWorkingDirectory Текущая рабочая директория.
     * @param path                    Путь к новой директории.
     * @return Новая текущая рабочая директория.
     * @throws IllegalArgumentException Если текущая рабочая директория или путь равны null.
     * @throws IllegalArgumentException Если путь не указывает на существующую директорию.
     * @throws IllegalArgumentException Если путь не является абсолютным или не может быть преобразован.
     */
    public static File changeDirectory(File currentWorkingDirectory, String path) {
        if (currentWorkingDirectory == null || path == null) {
            throw new IllegalArgumentException("Current working directory and path must not be null.");
        }

        try {
            Path newPath = Paths.get(path);
            if (!newPath.isAbsolute()) {
                newPath = Paths.get(currentWorkingDirectory.getAbsolutePath() + File.separator + path);
            }

            File newDirectory = newPath.toAbsolutePath().normalize().toFile();
            if (!newDirectory.exists() || !newDirectory.isDirectory()) {
                throw new IllegalArgumentException(path + " is not a valid directory");
            }
            return newDirectory;
        } catch (InvalidPathException e) {
            throw new IllegalArgumentException("Invalid path: " + path, e);
        } catch (UnsupportedOperationException e) {
            throw new IllegalArgumentException("Unsupported path: " + path, e);
        }
    }

    /**
     * Получает размер файла или директории.
     *
     * @param file Файл или директория.
     * @return Размер файла или директории.
     * @throws IllegalArgumentException Если файл равен null.
     */
    public static long getFileSize(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null.");
        }

        if (file.isFile()) {
            return file.length();
        } else {
            long result = 0;
            for (File innerFile : file.listFiles()) {
                result += getFileSize(innerFile);
            }
            return result;
        }
    }

    /**
     * Форматирует размер файла в удобочитаемый вид.
     *
     * @param size Размер файла.
     * @return Отформатированный размер файла.
     */
    private static String formatFileSize(long size) {
        int formatIndex = 0;
        double sizeD = (double) size;
        while (formatIndex < FILE_SIZE_FORMATS.length) {
            double newSizeD = sizeD / 1024;
            if (newSizeD < 1) break;
            sizeD = newSizeD;
            formatIndex++;
        }

        return new DecimalFormat("0.00").format(sizeD) + " " + FILE_SIZE_FORMATS[formatIndex];
    }
}
