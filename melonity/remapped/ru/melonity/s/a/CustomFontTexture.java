// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.s.a;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.util.math.ColorHelper;
import org.lwjgl.system.MemoryUtil;
import ru.melonity.fabric.client.model.INativeImage;

@Environment(value=EnvType.CLIENT)
public class CustomFontTexture {
    public final Identifier textureIdentifier;
    private final Char2ObjectArrayMap<Glyph> glyphMap = new Char2ObjectArrayMap();
    private final char startChar;
    private final char endChar;
    private final Font font;
    private final int padding;
    public int textureWidth;
    public int textureHeight;
    private boolean textureGenerated = false;

    public CustomFontTexture(char startChar, char endChar, Font font, Identifier textureIdentifier, int padding) {
        this.startChar = startChar;
        this.endChar = endChar;
        this.font = font;
        this.textureIdentifier = textureIdentifier;
        this.padding = padding;
    }

    public static void uploadTexture(Identifier textureIdentifier, BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
        long pointer = ((INativeImage)nativeImage).pointer();
        int rowStride = nativeImage.getWidth();
        int pixelCount = nativeImage.getHeight();
        IntBuffer buffer = MemoryUtil.memIntBuffer(pointer, rowStride * pixelCount);
        WritableRaster raster = image.getRaster();
        ColorModel colorModel = image.getColorModel();
        Object dataArray = getDataArray(raster);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster.getDataElements(x, y, dataArray);
                int alpha = colorModel.getAlpha(dataArray);
                int red = colorModel.getRed(dataArray);
                int green = colorModel.getGreen(dataArray);
                int blue = colorModel.getBlue(dataArray);
                int color = ColorHelper.Argb.getArgb(alpha, blue, green, red);
                buffer.put(color);
            }
        }
        NativeImageBackedTexture texture = new NativeImageBackedTexture(nativeImage);
        texture.upload();
        if (RenderSystem.isOnRenderThread()) {
            MinecraftClient client = MinecraftClient.getInstance();
            TextureManager textureManager = client.getTextureManager();
            textureManager.registerTexture(textureIdentifier, (AbstractTexture)texture);
        } else {
            RenderSystem.recordRenderCall(() -> {
                MinecraftClient client = MinecraftClient.getInstance();
                TextureManager textureManager = client.getTextureManager();
                textureManager.registerTexture(textureIdentifier, (AbstractTexture)texture);
            });
        }
    }

    private static Object getDataArray(WritableRaster raster) {
        DataBuffer dataBuffer = raster.getDataBuffer();
        int dataType = dataBuffer.getDataType();
        return switch (dataType) {
            case 0 -> new byte[raster.getNumDataElements()];
            case 1 -> new short[raster.getNumDataElements()];
            case 3 -> new int[raster.getNumDataElements()];
            default -> throw new IllegalArgumentException("Unsupported data buffer type: " + dataType);
        };
    }

    public Glyph getGlyph(char character) {
        if (!this.textureGenerated) {
            this.generateTexture();
        }
        return this.glyphMap.get(character);
    }

    public boolean containsCharacter(char character) {
        return character >= this.startChar && character < this.endChar;
    }

    private Font getFontForCharacter(char character) {
        if (this.font.canDisplay(character)) {
            return this.font;
        } else {
            int size = this.font.getSize();
            return new Font("SansSerif", 0, size);
        }
    }

    public void generateTexture() {
        if (this.textureGenerated) {
            return;
        }
        int charCount = this.endChar - this.startChar - 1;
        double sqrt = Math.sqrt(charCount);
        double columns = Math.ceil(sqrt);
        int maxColumns = (int)(columns * 1.5);
        this.glyphMap.clear();
        int currentX = 0;
        int currentY = 0;
        int maxRowWidth = 0;
        int maxRowHeight = 0;
        int currentRowHeight = 0;
        int rowCount = 0;
        int maxGlyphHeight = 0;
        ArrayList<Glyph> glyphList = new ArrayList<Glyph>();
        AffineTransform transform = new AffineTransform();
        FontRenderContext renderContext = new FontRenderContext(transform, true, false);
        int charIndex = 0;
        while (charIndex <= charCount) {
            char character = (char)(this.startChar + charIndex);
            Font charFont = this.getFontForCharacter(character);
            String charString = String.valueOf(character);
            Rectangle2D bounds = charFont.getStringBounds(charString, renderContext);
            double charWidth = bounds.getWidth();
            int width = (int)Math.ceil(charWidth);
            double charHeight = bounds.getHeight();
            int height = (int)Math.ceil(charHeight);
            charIndex++;
            maxRowWidth = Math.max(maxRowWidth, currentX + width);
            maxRowHeight = Math.max(maxRowHeight, currentY + height);
            if (rowCount >= maxColumns) {
                currentX = 0;
                currentY += maxGlyphHeight + this.padding;
                rowCount = 0;
                maxGlyphHeight = 0;
            }
            maxGlyphHeight = Math.max(maxGlyphHeight, height);
            Glyph glyph = new Glyph(currentX, currentY, width, height, character, this);
            glyphList.add(glyph);
            currentX += width + this.padding;
            rowCount++;
        }
        int textureWidth = Math.max(maxRowWidth + this.padding, 1);
        int textureHeight = Math.max(maxRowHeight + this.padding, 1);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        BufferedImage textureImage = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = textureImage.createGraphics();
        Color transparent = new Color(255, 255, 255, 0);
        graphics.setColor(transparent);
        graphics.fillRect(0, 0, this.textureWidth, this.textureHeight);
        graphics.setColor(Color.WHITE);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Iterator<Glyph> iterator = glyphList.iterator();
        while (iterator.hasNext()) {
            Glyph glyph = iterator.next();
            char character = glyph.getCharacter();
            Font charFont = this.getFontForCharacter(character);
            graphics.setFont(charFont);
            FontMetrics metrics = graphics.getFontMetrics();
            String charString = String.valueOf(character);
            int x = glyph.getX();
            int y = glyph.getY();
            int ascent = metrics.getAscent();
            graphics.drawString(charString, x, y + ascent);
            this.glyphMap.put(character, glyph);
        }
        uploadTexture(this.textureIdentifier, textureImage);
        this.textureGenerated = true;
    }

    @Environment(value=EnvType.CLIENT)
    public class Glyph {
        private final int x;
        private final int y;
        private final int width;
        private final int height;
        private final char character;
        private final CustomFontTexture fontTexture;

        public Glyph(int x, int y, int width, int height, char character, CustomFontTexture fontTexture) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.character = character;
            this.fontTexture = fontTexture;
        }

        public char getCharacter() {
            return this.character;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }
    }
}