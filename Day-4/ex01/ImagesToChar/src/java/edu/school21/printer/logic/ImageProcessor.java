package edu.school21.printer.logic;


import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageProcessor {
    private static final String IMAGE_PATH = "/resources/image.bmp";
    private static final String UNABLE_TO_READ_IMAGE_ERROR = "Unable to read the image";
    private static final String UNSUPPORTED_COLOR_ERROR = "Unsupported color";

    private char black;
    private char white;

    public ImageProcessor(char black, char white) {
        this.black = black;
        this.white = white;
    }

    public void printImage() {
        try {
            BufferedImage image;
            try {
                image = ImageIO.read(ImageProcessor.class.getResource(IMAGE_PATH));
            } catch (IOException e) {
                throw new RuntimeException("Error reading image file: " + e.getMessage());
            }

            if (image != null) {
                printImage(image);
            } else {
                throw new RuntimeException(UNABLE_TO_READ_IMAGE_ERROR);
            }

        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
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
