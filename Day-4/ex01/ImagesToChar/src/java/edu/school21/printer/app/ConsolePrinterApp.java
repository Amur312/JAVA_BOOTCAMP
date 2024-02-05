package edu.school21.printer.app;

import edu.school21.printer.logic.ImageProcessor;

public class ConsolePrinterApp {
    private static final String ERROR_MESSAGE = "Error\n" +
            "Usage: ConsolePrinterApp.java --black=[BLACK_SYMBOL] --white=[WHITE_SYMBOL]";

    public static void main(String[] args) {
        try {
            String[] parsedArgs = parseArgs(args);
            new ImageProcessor(parsedArgs[0].charAt(0), parsedArgs[1].charAt(0)).printImage();
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static String[] parseArgs(String[] args) {
        validateArgsLength(args);

        String[] parsedArgs = new String[]{
                parseArg(args[0], "--black"),
                parseArg(args[1], "--white")
        };

        validateSymbolLength(parsedArgs[0], parsedArgs[1]);

        return parsedArgs;
    }

    private static void validateArgsLength(String[] args) {
        if (args.length != 2) {
            throw new RuntimeException(ERROR_MESSAGE);
        }
    }

    private static String parseArg(String arg, String awaitingKey) {
        int eqPos = arg.indexOf('=');
        if (eqPos == -1) {
            throw new RuntimeException("Invalid argument format: " + arg);
        }

        String key = arg.substring(0, eqPos);
        String value = arg.substring(eqPos + 1);
        if (!key.equals(awaitingKey)) {
            throw new RuntimeException("Invalid argument key: " + key);
        }
        return value;
    }

    private static void validateSymbolLength(String blackSymbol, String whiteSymbol) {
        if (blackSymbol.length() != 1 || whiteSymbol.length() != 1) {
            throw new RuntimeException("Symbol length must be 1");
        }
    }
}
