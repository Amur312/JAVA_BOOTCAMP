package edu.school21.printer.app;

import java.io.File;

import edu.school21.printer.logic.ImageProcessor;

public class ConsolePrinterApp {
    private static final String ERROR_MESSAGE = "Error\n" +
            "Usage: ConsolePrinterApp.java --black=[BLACK_SYMBOL] --white=[WHITE_SYMBOL] --image=[FILE_PATH]";

    public static void main(String[] args) {
        try {
            String[] parsedArgs = parseArgs(args);
            new ImageProcessor(parsedArgs[0].charAt(0), parsedArgs[1].charAt(0), parsedArgs[2]).printImage();
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static String[] parseArgs(String[] args) {
        validateArgsLength(args);

        String[] parsedArgs = new String[]{
                parseArg(args[0], "--black"),
                parseArg(args[1], "--white"),
                parseArg(args[2], "--image")
        };

        validateSymbols(parsedArgs[0], parsedArgs[1]);
        validateFileExists(parsedArgs[2]);

        return parsedArgs;
    }

    private static void validateArgsLength(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
    }

    private static String parseArg(String arg, String expectedPrefix) {
        if (!arg.startsWith(expectedPrefix + "=")) {
            throw new IllegalArgumentException("Invalid argument format: " + arg);
        }
        return arg.substring(expectedPrefix.length() + 1);
    }

    private static void validateSymbols(String blackSymbol, String whiteSymbol) {
        if (blackSymbol.length() != 1 || whiteSymbol.length() != 1) {
            throw new IllegalArgumentException("Symbol length must be 1");
        }
    }

    private static void validateFileExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("File doesn't exist: " + filePath);
        }
    }
}
