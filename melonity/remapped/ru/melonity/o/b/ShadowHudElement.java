// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.o.b;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import ru.melonity.s.c.HudRenderer;
import ru.melonity.s.e.BaseHudElement;

import java.awt.Color;

@Environment(EnvType.CLIENT)
public class ShadowHudElement extends BaseHudElement {

    public ShadowHudElement() {
        super(94.0f, 33.0f);
    }

    @Override
    public void render(float x, float y, double mouseX, double mouseY, HudRenderer renderer, MatrixStack matrices) {
        renderer.drawRoundedRectangle(x, y, this.width, this.height, 12.0f, Color.decode("#181818"), matrices);
    }

    @Override
    public void onMouseMoved(float x, float y, double mouseX, double mouseY, int button) {}

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        return false;
    }

    @Override
    public void onClose() {}
}