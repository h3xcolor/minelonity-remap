// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.v.a;

import java.awt.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;
import ru.melonity.s.a.IIllIIIllIllIIIlllIIIlllIIllIIlllIlIIIllIllIlIIlIIIlllIIlIIllIIllIlIllIIllIIIllIIlIllIIlIIlIIllIIlIlIIllIllIIlIlIIlIIIllIIlIllIllIIlllIllIIlIlllIIllIlIlllIIlllIIIlIIlIIllIlllIlIIllIIIllIlIlIIlIIllIllIlIllIIIllIIll as TextRenderer;

@Environment(value=EnvType.CLIENT)
public interface Drawer {
    public static int RENDERER_VERSION = 1080760593;

    void drawShape(Vector2f position, Vector2f size, float rotation, Color color);

    void drawTexture(Identifier textureId, Vector2f position, Vector2f size, Color color);

    void drawText(String text, TextRenderer textRenderer, Vector2f position, Color color);

    default void drawShape(Vector2f position, Vector2f size, Color color) {
        this.drawShape(position, size, 0.0f, color);
    }

    default void drawTexture(Identifier textureId, Vector2f position, Vector2f size) {
        this.drawTexture(textureId, position, size, Color.WHITE);
    }

    default void drawText(String text, TextRenderer textRenderer, Vector2f position) {
        this.drawText(text, textRenderer, position, Color.WHITE);
    }
}