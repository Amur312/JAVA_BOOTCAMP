package edu.school21.printer.logic;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageProcessor {
    private static final String FILE_PATH_EMPTY_ERROR = "File path cannot be empty";
    private static final String UNSUPPORTED_COLOR_ERROR = "Unsupported color";

    private char black;
    private char white;
    private String filePath;

    public ImageProcessor(char black, char white, String filePath) {
        this.black = black;
        this.white = white;
        this.filePath = filePath;
    }

    public void printImage() {
        try {
            validateFilePath();

            BufferedImage image;
            try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(filePath))) {
                image = ImageIO.read(input);
            }

            if (image != null) {
                printImage(image);
            } else {
                System.err.println("Error: Unable to read the image");
            }

        } catch (IOException e) {
            System.err.println("Error reading image file: " + e.getMessage());
        }
    }

    private void validateFilePath() {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException(FILE_PATH_EMPTY_ERROR);
        }
    }

    private void printImage(BufferedImage image) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int color = image.getRGB(x, y);
                if (color == Color.BLACK.getRGB()) {
                    System.out.print(black);
                } else if (color == Color.WHITE.getRGB()) {
                    System.out.print(white);
                } else {
                    throw new RuntimeException(UNSUPPORTED_COLOR_ERROR);
                }
            }
            System.out.println();
        }
    }
}
