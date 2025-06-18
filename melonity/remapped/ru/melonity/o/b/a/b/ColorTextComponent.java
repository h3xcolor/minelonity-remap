// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.o.b.a.b;

import java.awt.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import ru.melonity.Melonity;
import ru.melonity.o.b.a.Component;

@Environment(EnvType.CLIENT)
public class ColorTextComponent implements Component {
    private final String text;
    private Color color;

    public ColorTextComponent(String text) {
        this.text = text;
        this.color = Melonity.COLOR_MANAGER.getColor(1, 60);
    }

    public ColorTextComponent(String text, Color color) {
        this.text = text;
        this.color = color;
    }

    @Override
    public void render(Component parent, float x, float y, float width, double mouseX, double mouseY, Component helper, MatrixStack matrices, TextRenderer textRenderer) {
        helper.renderText(ru.melonity.i.TextRenderer.LEFT_ALIGNED, this.getTextRenderer().getText(this.text, new Object[0]), x, y + 4.0f, matrices);
        helper.renderRoundedRectangle(x + width - 12.0f - 18.0f, y, 12.0f, 12.0f, 3.0f, this.color, matrices);
    }

    @Override
    public void onMouseClick(float x, float y, float width, double mouseX, double mouseY, int button) {
        boolean isMouseOver = ru.melonity.w.MouseHelper.isMouseOver(x + width - 12.0f - 18.0f, y, 12.0, 12.0, mouseX, mouseY);
        if (isMouseOver) {
            this.color = Melonity.COLOR_MANAGER.getColor(1, 60);
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        return false;
    }

    @Override
    public void onRemoved() {
    }

    @Override
    public float getHeight() {
        return 24.0f;
    }

    @Override
    public void onAdded() {
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getText() {
        return this.text;
    }

    public Color getColor() {
        return this.color;
    }
}