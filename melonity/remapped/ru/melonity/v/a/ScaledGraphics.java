// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.v.a;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;
import ru.melonity.Melonity;
import ru.melonity.s.a.FontProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.awt.Color;

@Environment(EnvType.CLIENT)
public class ScaledGraphics implements GraphicsRenderer {
    private final GraphicsRenderer graphics = Melonity.getMainGraphics();
    private float scale = 1.0f;
    private final MatrixStack matrixStack;
    public static int SCALING_ID = 335821378;

    public ScaledGraphics(MatrixStack matrixStack) {
        this.matrixStack = matrixStack;
    }

    @Override
    public void drawRoundedQuad(Vector2f position, Vector2f size, float radius, Color color) {
        this.graphics.drawRoundedQuad(
            position.x, 
            position.y, 
            size.x * this.scale, 
            size.y * this.scale, 
            radius, 
            color, 
            this.matrixStack
        );
    }

    @Override
    public void drawTexture(Identifier textureId, Vector2f position, Vector2f size, Color color) {
        this.graphics.drawTexture(
            textureId, 
            position.x, 
            position.y, 
            size.x * this.scale, 
            size.y * this.scale, 
            color, 
            this.matrixStack
        );
    }

    @Override
    public void drawText(String text, FontProvider font, Vector2f position, Color color) {
        this.graphics.drawText(
            font, 
            text, 
            position.x, 
            position.y, 
            color, 
            this.matrixStack
        );
    }

    public float getScale() {
        return this.scale;
    }

    public ScaledGraphics setScale(float scale) {
        this.scale = scale;
        return this;
    }
}