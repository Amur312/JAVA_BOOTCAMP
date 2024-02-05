package ex02;

public class Program {

    private static final String USER_DIR = "user.dir";
    private static final String CURRENT_FOLDER_FLAG = "--current-folder";

    public static void main(String[] args) {
        try {
            String startFolderPath = getStartFolderPath(args);
            FileManager fileManager = new FileManager(startFolderPath);
            fileManager.run();
        } catch (Exception e) {
            printErrorMessage(e);
            System.exit(-1);
        }
    }

    /**
     * Получает путь к начальной директории из аргументов командной строки.
     *
     * @param args Аргументы командной строки.
     * @return Путь к начальной директории.
     */
    private static String getStartFolderPath(String[] args) {
        if (args.length == 0) {
            return System.getProperty(USER_DIR);
        } else {
            return parseInputArgument(args[0]);
        }
    }

    /**
     * Разбирает аргумент командной строки для получения пути к начальной директории.
     *
     * @param arg Аргумент командной строки.
     * @return Путь к начальной директории.
     * @throws RuntimeException Если аргумент имеет неверный формат.
     */
    private static String parseInputArgument(String arg) {
        String errorMessage = "Invalid argument. Usage: Program.java " + CURRENT_FOLDER_FLAG + "= FOLDER_PATH   ";

        int equalsIndex = arg.indexOf('=');
        if (equalsIndex == -1) {
            throw new RuntimeException(errorMessage);
        }

        String flag = arg.substring(0, equalsIndex);
        String folderPath = arg.substring(equalsIndex + 1);

        if (!flag.equals(CURRENT_FOLDER_FLAG)) {
            throw new RuntimeException(errorMessage);
        }

        return folderPath;
    }

    /**
     * Выводит сообщение об ошибке в стандартный поток ошибок.
     *
     * @param e Исключение, вызванное ошибкой.
     */
    private static void printErrorMessage(Exception e) {
        System.err.println(e.getMessage());
    }

}
