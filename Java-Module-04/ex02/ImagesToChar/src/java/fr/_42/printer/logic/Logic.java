package fr._42.printer.logic;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;

public class Logic {
    private final Ansi.BColor whiteColor;
    private final Ansi.BColor blackColor;

    public Logic(String white, String black) {
        this.whiteColor = Ansi.BColor.valueOf(white.toUpperCase());
        this.blackColor = Ansi.BColor.valueOf(black.toUpperCase());
    }

    public void printImage() {
        try (
            InputStream stream = Logic.class.getResourceAsStream("/resources/it.bmp")
        ) {
            if (stream == null) {
                throw new IllegalStateException("Image resource not found.");
            }

            BufferedImage image = ImageIO.read(stream);
            if (image == null) {
                throw new IllegalStateException("Invalid image.");
            }

            ColoredPrinter printer = new ColoredPrinter.Builder(1, false).build();
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int rgb = image.getRGB(x, y) & 0xFFFFFF;
                    Ansi.BColor color;
                    
                    if (rgb == 0xFFFFFF) {
                        color = whiteColor;
                    } else {
                        color = blackColor;
                    }
                    
                    printer.print(
                            "  ",
                            Ansi.Attribute.BOLD,
                            Ansi.FColor.WHITE,
                            color
                    );
                }

                System.out.println();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image.", e);
        }
    }
}
