package edu.school21.printer.app;

import edu.school21.printer.logic.ImageProcessor;
import com.beust.jcommander.*;

@Parameters(separators = "=")
public class ConsolePrinterApp {
    @Parameter(names = "--black", required = true, description = "Symbol for black pixels")
    private static String blackSymbol;

    @Parameter(names = "--white", required = true, description = "Symbol for white pixels")
    private static String whiteSymbol;

    public static void main(String[] args) {
        try {
            ConsolePrinterApp app = new ConsolePrinterApp();
            JCommander.newBuilder()
                    .addObject(app)
                    .build()
                    .parse(args);

            app.runImageProcessor();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private void runImageProcessor() {
        new ImageProcessor(blackSymbol, whiteSymbol).printImage();
    }
}
