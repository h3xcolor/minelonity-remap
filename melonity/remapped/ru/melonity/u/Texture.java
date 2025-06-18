// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.u;

import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

@Environment(value = EnvType.CLIENT)
public class Texture {
    private static final int BYTES_PER_PIXEL = 4;
    private final ImageProvider imageProvider;
    private Integer textureId;

    public void bind(int textureUnit) {
        GL33.glActiveTexture(33984 + textureUnit);
        GL33.glBindTexture(3553, getTextureId());
    }

    public void deleteTexture() {
        GL33.glDeleteTextures(textureId);
        textureId = null;
    }

    public int getTextureId() {
        if (textureId != null) {
            return textureId;
        }
        textureId = generateTexture();
        return textureId;
    }

    private int generateTexture() {
        BufferedImage image = imageProvider.getImage();
        int width = image.getWidth();
        int height = image.getHeight();
        ByteBuffer buffer = MemoryUtil.memAlloc(width * height * 4);
        ByteBuffer orderedBuffer = buffer.order(ByteOrder.BIG_ENDIAN);
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                orderedBuffer.putInt(pixel << 8 | pixel >>> 24);
            }
        }
        int texture = GL33.glGenTextures();
        GL33.glBindTexture(3553, texture);
        GL33.glTexParameteri(3553, 33084, 0);
        GL33.glTexParameteri(3553, 33085, 4);
        GL33.glTexParameteri(3553, 10241, 9987);
        GL33.glTexParameteri(3553, 10240, 9729);
        GL33.glTexImage2D(3553, 0, 6408, width, height, 0, 6408, 5121, orderedBuffer.position(0));
        GL33.glGenerateMipmap(3553);
        MemoryUtil.memFree(orderedBuffer);
        return texture;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Texture other = (Texture) obj;
        return Objects.equals(textureId, other.textureId);
    }

    public int hashCode() {
        return Objects.hash(textureId);
    }

    public Texture(ImageProvider imageProvider) {
        this.imageProvider = imageProvider;
    }
}

interface ImageProvider {
    BufferedImage getImage();
}