// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.i;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import ru.melonity.s.a.CustomFont;
import ru.melonity.s.a.FontType;

@Environment(value = EnvType.CLIENT)
public class FontLoader {
    private static final FontType STANDARD_FONT = FontType.STANDARD;
    private static final FontType SMALL_FONT = FontType.SMALL;
    private static final FontType OTHER_FONT = FontType.OTHER;

    public static final CustomFont FONT_12 = createFont(STANDARD_FONT, 12.0f);
    public static final CustomFont FONT_13 = createFont(STANDARD_FONT, 13.0f);
    public static final CustomFont FONT_14 = createFont(STANDARD_FONT, 14.0f);
    public static final CustomFont FONT_15 = createFont(STANDARD_FONT, 15.0f);
    public static final CustomFont FONT_16 = createFont(STANDARD_FONT, 16.0f);
    public static final CustomFont FONT_18 = createFont(STANDARD_FONT, 18.0f);
    public static final CustomFont SMALL_FONT_8 = createFont(SMALL_FONT, 8.0f);
    public static final CustomFont SMALL_FONT_10 = createFont(SMALL_FONT, 10.0f);
    public static final CustomFont SMALL_FONT_12 = createFont(SMALL_FONT, 12.0f);
    public static final CustomFont SMALL_FONT_13 = createFont(SMALL_FONT, 13.0f);
    public static final CustomFont SMALL_FONT_14 = createFont(SMALL_FONT, 14.0f);
    public static final CustomFont SMALL_FONT_15 = createFont(SMALL_FONT, 15.0f);
    public static final CustomFont OTHER_FONT_15 = createFont(OTHER_FONT, 15.0f);

    private static CustomFont createFont(FontType fontType, float fontSize) throws RuntimeException {
        MinecraftClient client = MinecraftClient.getInstance();
        ResourceManager resourceManager = client.getResourceManager();
        String fileName = fontType.getFileName();
        String path = "melonity/fonts/" + fileName;
        Identifier identifier = new Identifier(path);
        try (InputStream inputStream = resourceManager.open(identifier)) {
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            Font derivedFont = baseFont.deriveFont(Font.PLAIN, fontSize / 2.0f);
            return new CustomFont(derivedFont, fontSize / 2.0f);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load font: " + fileName, e);
        }
    }
}