// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.u;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ImageProvider {
    BufferedImage image();

    default BufferedImage getStandardImage() {
        BufferedImage originalImage = this.image();
        if (originalImage.getType() == BufferedImage.TYPE_INT_ARGB) {
            return originalImage;
        }
        BufferedImage convertedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = convertedImage.createGraphics();
        graphics.drawImage(originalImage, 0, 0, null);
        graphics.dispose();
        return convertedImage;
    }

    static ImageProvider fromFile(String filePath) {
        return () -> {
            try {
                return ImageIO.read(new File(filePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    static ImageProvider fromInputStream(InputStream inputStream) {
        return () -> {
            try {
                return ImageIO.read(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}