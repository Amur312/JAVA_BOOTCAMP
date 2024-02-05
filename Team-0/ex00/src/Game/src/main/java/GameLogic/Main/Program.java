package GameLogic.Main;

import GameLogic.controllers.GameController;
import GameLogic.services.GameConfigurationArgs;
import com.beust.jcommander.JCommander;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Program {

    public static void main(String[] args) {
        try {
            GameConfigurationArgs jargs = parseCommandLineArgs(args);

            String propertiesFileName = "application-" + jargs.getGameProfile() + ".properties";
            Path tempFile = createTempPropertiesFile(propertiesFileName);

            GameController gameController = new GameController(tempFile, jargs);
            gameController.startGame();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static GameConfigurationArgs parseCommandLineArgs(String[] args) {
        GameConfigurationArgs jargs = new GameConfigurationArgs();
        JCommander jCommander = new JCommander(jargs);
        jCommander.parse(args);
        return jargs;
    }

    private static Path createTempPropertiesFile(String propertiesFileName) throws IOException, IOException {
        try (InputStream inputStream = Program.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
            if (inputStream == null) {
                System.out.println("Properties file not found: " + propertiesFileName);
                throw new FileNotFoundException("Properties file not found: " + propertiesFileName);
            }

            Path tempFile = Files.createTempFile("temp", ".properties");
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

            return tempFile;
        }
    }
}