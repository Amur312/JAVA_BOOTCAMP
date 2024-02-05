package GameLogic.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PropertiesFileParser {
    private static final String DELIMITER_REGEX = "\\s+";
    private static final String DEFAULT_PROPERTY_VALUE = " ";

    public static Map<String, String> parseProperties(Path propertiesFilePath) throws FileNotFoundException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(propertiesFilePath.toFile()))) {
            return bufferedReader.lines()
                    .map(line -> line.split(DELIMITER_REGEX))
                    .collect(Collectors.toMap(
                            tokens -> tokens[0],
                            tokens -> (tokens.length == 3) ? tokens[2] : DEFAULT_PROPERTY_VALUE
                    ));
        } catch (Exception e) {
            throw new FileNotFoundException("Failed to read properties file: " + e.getMessage());
        }
    }
}
