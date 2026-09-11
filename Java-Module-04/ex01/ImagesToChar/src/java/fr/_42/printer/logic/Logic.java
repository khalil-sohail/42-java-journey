package fr._42.printer.logic;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

public class Logic {
    private char[][]    imageData;
    private char        whiteChar;
    private char        blackChar;
    private InputStream inputStream;

    public Logic(char whiteChar, char blackChar) {
        this.whiteChar = whiteChar;
        this.blackChar = blackChar;
        this.inputStream = Logic.class.getResourceAsStream("/resources/it.bmp");
    }

    public void convertImage() throws IllegalArgumentException, RuntimeException {
        try {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                throw new IllegalArgumentException("Invalid image file.");
            }

            int width = image.getWidth();
            int height = image.getHeight();
            imageData = new char[height][width];
            
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y) & 0xFFFFFF;

                    if (rgb == 0xFFFFFF) {
                        imageData[y][x] = whiteChar;
                    } else {
                        imageData[y][x] = blackChar;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image.", e);
        }
    }

    public void printImage() throws IllegalStateException {
        if (imageData == null) {
            throw new IllegalStateException("Image must be converted before printing.");
        }

        for (char[] row : imageData) {
            System.out.println(row);
        }
    }
}
