package edu.school21.printer.logic;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import com.diogonunes.jcolor.*;

public class ImageProcessor {
    private static final String DEFAULT_COLOR_NAME = "white";
    private Attribute black;
    private Attribute white;

    public ImageProcessor(String black, String white) {
        this.black = stringToAttribute(black);
        this.white = stringToAttribute(white);
    }

    public void printImage() {
        try {
            BufferedImage image = ImageIO.read(ImageProcessor.class.getResource("/resources/image.bmp"));

            if (image == null) {
                throw new RuntimeException("Unable to read the image");
            }

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int color = image.getRGB(x, y);
                    if (color == Color.black.getRGB()) {
                        System.out.print(Ansi.colorize(" ", black));
                    } else if (color == Color.white.getRGB()) {
                        System.out.print(Ansi.colorize(" ", white));
                    } else {
                        throw new RuntimeException("Unsupported color");
                    }
                }
                System.out.print("\n");
            }
        } catch (NullPointerException | IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private Attribute stringToAttribute(String colorName) {
        switch (colorName.toLowerCase()) {
            case "cyan":
                return Attribute.CYAN_BACK();
            case "green":
                return Attribute.GREEN_BACK();
            case "magenta":
                return Attribute.MAGENTA_BACK();
            case "red":
                return Attribute.RED_BACK();
            case "yellow":
                return Attribute.YELLOW_BACK();
            default:
                return Attribute.WHITE_BACK();
        }
    }
}
