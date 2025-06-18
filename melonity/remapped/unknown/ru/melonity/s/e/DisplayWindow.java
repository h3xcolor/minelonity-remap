// ремапили ребята из https://t.me/dno_rumine
package ru.melonity.s.e;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_4587;

@Environment(EnvType.CLIENT)
public abstract class DisplayWindow {
    protected float width;
    protected float height;
    public static int DEFAULT_WINDOW_ID = 659092592;

    public abstract void draw(float x, float y, double mouseX, double mouseY, IIllIIlIllIIlIlllIIllIIIllIIIllIlIIlIIIllIIlllIIllIllIlIIlllIIlllIlIlIIlllIIlllIIIllIIllIIlIIIllIIIlllIIIllIllIIIlllIIllIllIlllIIIllIIIllIIlIlIIllIIlIIIlllIIllIIllIllIlllIIIlIIlIIIlIIllIlIIllIlllIlIIIllIIIlIIIllIIIlIlIllIIllIlIll state, class_4587 matrixStack);

    public abstract void drawOverlay(float x, float y, double mouseX, double mouseY, int overlay);

    public abstract boolean onMouseClick(int mouseX, int mouseY, int button);

    public abstract void initialize();

    public DisplayWindow(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}