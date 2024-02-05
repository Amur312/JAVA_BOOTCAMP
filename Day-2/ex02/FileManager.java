package ex02;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileManager {
    private File cwd;

    /**
     * Конструктор класса FileManager.
     *
     * @param startDir Начальная директория.
     * @throws IllegalArgumentException Если начальная директория не существует или не является директорией.
     */
    public FileManager(String startDir) {
        File cwd = new File(startDir);

        if (!cwd.exists()) {
            throw new IllegalArgumentException(startDir + " does not exist");
        }

        if (!cwd.isDirectory()) {
            throw new IllegalArgumentException(startDir + " is not a directory");
        }

        this.cwd = cwd;
    }

    /**
     * Запускает интерфейс командной строки для взаимодействия с файловой системой.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            makeCall(scanner.nextLine());
        }
    }

    /**
     * Вызывает соответствующий метод обработки команды.
     *
     * @param inputLine Введенная команда.
     */
    private void makeCall(String inputLine) {
        if (inputLine.length() == 0) return;

        String[] args = inputLine.split(" ");
        String cmd = args[0].toUpperCase();

        try {
            Command command = Command.valueOf(cmd);
            switch (command) {
                case MV:
                    move(args);
                    break;
                case LS:
                    listFiles(args);
                    break;
                case CD:
                    changeDirectory(args);
                    break;
                case EXIT:
                    exitProgram(args);
                    break;
                default:
                    System.err.println("Unknown command: " + cmd);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid command: " + cmd);
        }
    }

    /**
     * Перемещает файл или директорию.
     *
     * @param args Аргументы команды.
     */
    private void move(String[] args) {
        if (args.length != 3) return;

        try {
            Path sourcePath = Paths.get(args[1]);
            Path destinationPath = Paths.get(args[2]);
            if (!sourcePath.isAbsolute()) sourcePath = Paths.get(cwd.getAbsolutePath() + File.separator + args[1]);
            if (!destinationPath.isAbsolute())
                destinationPath = Paths.get(cwd.getAbsolutePath() + File.separator + args[2]);

            File sourceFile = sourcePath.toAbsolutePath().normalize().toFile();
            File destinationFile = destinationPath.toAbsolutePath().normalize().toFile();

            FileService.move(sourceFile, destinationFile);
        } catch (InvalidPathException | UnsupportedOperationException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Выводит содержимое текущей директории.
     *
     * @param args Аргументы команды.
     */
    private void listFiles(String[] args) {
        FileService.displayContent(cwd);
    }

    /**
     * Изменяет текущую директорию.
     *
     * @param args Аргументы команды.
     */
    private void changeDirectory(String[] args) {
        if (args.length != 2) return;
        String filePath = args[1];

        cwd = FileService.changeDirectory(cwd, filePath);
        System.out.println(cwd.getAbsolutePath());
    }

    /**
     * Завершает выполнение программы.
     *
     * @param args Аргументы команды.
     */
    private void exitProgram(String[] args) {
        System.exit(0);
    }
}
