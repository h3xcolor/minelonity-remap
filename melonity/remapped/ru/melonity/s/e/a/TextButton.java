// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.s.e.a;

import java.awt.Color;
import lombok.Generated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import ru.melonity.i.FontRenderer;
import ru.melonity.s.c.DrawHelper;
import ru.melonity.s.e.ButtonBase;

@Environment(value = EnvType.CLIENT)
public class TextButton extends ButtonBase {
    private final String text;
    private boolean highlighted = false;

    public TextButton(String text, float width, float height) {
        super(width, height);
        this.text = text;
    }

    @Override
    public void render(float x, float y, double mouseX, double mouseY, DrawHelper drawHelper, MatrixStack matrixStack) {
        Color color = Color.decode("#2C2C2C");
        drawHelper.drawRectangle(x, y, this.width, this.height, 4.0f, color, matrixStack);
        drawHelper.drawRectangle(x + 0.5f, y + 0.5f, this.width - 1.0f, this.height - 1.0f, 4.0f, Color.decode(this.highlighted ? "#2C2C2C" : "#222222"), matrixStack);
        drawHelper.drawRectangle(x + 0.5f, y + this.height - 3.0f, this.width - 1.0f, 3.0f, 4.0f, Color.decode("#2C2C2C"), matrixStack);
        FontRenderer fontRenderer = FontRenderer.INSTANCE;
        drawHelper.drawString(fontRenderer, this.text, x + this.width / 2.0f - fontRenderer.getStringWidth(this.text) / 2.0f, y + this.height / 2.0f - fontRenderer.getStringHeight(this.text) / 2.0f, Color.WHITE, matrixStack);
    }

    @Override
    public void mouseReleased(float x, float y, double mouseX, double mouseY, int button) {
    }

    @Override
    public boolean mouseClicked(float x, float y, double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public void tick() {
    }

    @Generated
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }
}